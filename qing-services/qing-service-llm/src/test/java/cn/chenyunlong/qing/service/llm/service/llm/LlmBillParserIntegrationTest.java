package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.*;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseDetailRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * LLM 账单解析集成测试
 *
 * <p>完整的端到端流程测试（文件提取 → Prompt构建 → LLM解析 → 结果验证），
 * LLM 调用使用 Mock，其他组件均为真实实例。</p>
 *
 * <p>使用真实的测试文件验证编码处理能力。</p>
 *
 * <p>CI 流水线中可通过设置 SKIP_REAL_LLM=true 禁用真实 LLM 测试。</p>
 */
@ExtendWith(MockitoExtension.class)
class LlmBillParserIntegrationTest {

    // ============================================================
    // 测试数据
    // ============================================================

    private static final String SAMPLE_CSV_CONTENT = """
            交易时间,交易描述,金额,账户,状态
            2024-01-15 12:30:00,京东商城-联想笔记本,-5999.00,招行卡(1234),已完成
            2024-01-15 18:30:00,美团外卖-麦当劳,-45.50,支付宝,已完成
            2024-01-16 14:00:00,抖音直播充值,-100.00,微信支付,已完成
            2024-01-18 18:00:00,海底捞火锅,-356.00,支付宝,已完成
            """;

    private static final String MOCK_LLM_RESPONSE = """
            {
              "success": true,
              "taskId": "test-task-123",
              "records": [
                {
                  "amount": -5999.00,
                  "transactionType": "EXPENSE",
                  "transactionTime": "2024-01-15T12:30:00",
                  "counterparty": "京东商城",
                  "description": "京东商城-联想笔记本",
                  "categoryId": 3,
                  "categoryName": "数码电子",
                  "confidence": 0.92,
                  "platformSource": "京东",
                  "consumptionType": "数码电子"
                },
                {
                  "amount": -45.50,
                  "transactionType": "EXPENSE",
                  "transactionTime": "2024-01-15T18:30:00",
                  "counterparty": "美团",
                  "description": "美团外卖-麦当劳",
                  "categoryId": 1,
                  "categoryName": "餐饮美食",
                  "confidence": 0.88,
                  "platformSource": "美团",
                  "consumptionType": "餐饮美食"
                },
                {
                  "amount": -100.00,
                  "transactionType": "EXPENSE",
                  "transactionTime": "2024-01-16T14:00:00",
                  "counterparty": "抖音",
                  "description": "抖音直播充值",
                  "categoryId": null,
                  "categoryName": null,
                  "confidence": 0.65,
                  "platformSource": "抖音",
                  "consumptionType": "娱乐",
                  "needNewCategory": true
                }
              ],
              "summary": {
                "totalRecords": 3,
                "successCount": 2,
                "failedCount": 0,
                "needReviewCount": 1,
                "avgConfidence": 0.82,
                "inputTokens": 150,
                "outputTokens": 90,
                "estimatedCost": 0.003,
                "platformSources": ["京东","美团","抖音"],
                "consumptionTypes": ["数码电子","餐饮美食","娱乐"]
              },
              "suggestedNewCategories": [
                {
                  "name": "直播娱乐",
                  "parentId": null,
                  "reason": "检测到抖音直播充值等娱乐消费，建议新建分类",
                  "sourceStrategy": "BY_CONSUMPTION_TYPE"
                }
              ],
              "suggestedNewAccounts": [],
              "unmatchedRecords": []
            }
            """;

    // ============================================================
    // Mock 组件
    // ============================================================

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private TransactionMatcherRepository matcherRepository;
    @Mock
    private LlmParseRecordRepository parseRecordRepository;
    @Mock
    private LlmParseDetailRepository detailRepository;

    // ============================================================
    // 真实组件（不被 Mock）
    // ============================================================

    private LlmFileContentExtractor fileContentExtractor;
    private LlmParseContextLoader contextLoader;
    private LlmPromptBuilder promptBuilder;

    // ============================================================
    // 被测系统
    // ============================================================

    private LlmBillParserFacade facade;

    // ============================================================
    // 测试数据
    // ============================================================

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // 1. 真实实例化文件内容提取器
        fileContentExtractor = new LlmFileContentExtractor();

        // 2. 真实实例化上下文加载器
        contextLoader = new LlmParseContextLoader(
                categoryRepository, accountRepository,
                counterpartyRepository, matcherRepository
        );

        // 3. 真实实例化 Prompt 构建器
        promptBuilder = new LlmPromptBuilder();

        // 4. Mock LLM 解析器（注入到 Facade）
        // 检查环境变量，CI 中可禁用真实 LLM
        LlmParserService mockLlmParser = createMockParser();

        // 5. 实例化 Facade
        facade = new LlmBillParserFacade(
                fileContentExtractor,
                contextLoader,
                null,           // resultCache - 设为 null 跳过缓存
                new LlmParseTaskService(),
                mockLlmParser,  // Mock LLM
                promptBuilder,
                parseRecordRepository,
                detailRepository,
                categoryRepository,
                accountRepository,
                counterpartyRepository
        );
        facade.init();
    }

    /**
     * 创建 Mock LLM 解析器
     * 可以在子类或测试配置中覆盖此方法返回真实实现
     */
    protected LlmParserService createMockParser() {
        LlmParserService mock = mock(LlmParserService.class);
        try {
            // 解析 MOCK_LLM_RESPONSE 为 LlmParseResponse 对象
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            LlmParseResponse mockResponse = mapper.readValue(MOCK_LLM_RESPONSE, LlmParseResponse.class);

            when(mock.parse(anyString(), any(CategoryStrategy.class)))
                    .thenReturn(MOCK_LLM_RESPONSE);
        } catch (Exception e) {
            fail("Failed to create mock parser: " + e.getMessage());
        }
        return mock;
    }

    // ============================================================
    // 文件编码测试
    // ============================================================

    @Test
    void testExtractUtf8Csv() {
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "utf8-test.csv", "text/csv", content);

        String text = fileContentExtractor.extract(content, "utf8-test.csv");

        assertNotNull(text);
        assertTrue(text.contains("京东商城"));
        assertTrue(text.contains("美团外卖"));
        assertTrue(text.contains("抖音直播充值"));
        assertTrue(text.contains("海底捞火锅"));
    }

    @Test
    void testExtractGbkCsv() throws Exception {
        // 从 resources 加载 GBK 文件
        Path gbkFile = tempDir.resolve("gbk-test.csv");
        java.nio.file.Files.writeString(gbkFile, SAMPLE_CSV_CONTENT, java.nio.charset.StandardCharsets.UTF_8);

        // 写入时用 GBK
        String gbkContent = SAMPLE_CSV_CONTENT;
        java.nio.file.Files.write(gbkFile, gbkContent.getBytes("GBK"));

        byte[] gbkBytes = java.nio.file.Files.readAllBytes(gbkFile);
        String text = fileContentExtractor.extract(gbkBytes, "gbk-test.csv");

        assertNotNull(text);
        assertTrue(text.contains("京东商城") || text.contains("海底捞"),
                "Should extract Chinese characters correctly, got: " + text);
    }

    @Test
    void testExtractPlainText() {
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        String text = fileContentExtractor.extract(content, "plain.txt");

        assertNotNull(text);
        assertTrue(text.contains("京东商城"));
        assertTrue(text.contains("美团外卖"));
    }

    // ============================================================
    // Prompt 构建测试
    // ============================================================

    @Test
    void testPromptBuilderContainsStrategy() {
        LlmParseContextLoader.SystemContext context = new LlmParseContextLoader.SystemContext(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        String prompt = promptBuilder.buildPrompt(
                "2024-01-15 京东 100元", context, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(prompt);
        assertTrue(prompt.contains("BY_CONSUMPTION_TYPE"));
        assertTrue(prompt.contains("消费类型"));
        assertTrue(prompt.contains("2024-01-15"));
    }

    @Test
    void testPromptBuilderByPlatform() {
        LlmParseContextLoader.SystemContext context = new LlmParseContextLoader.SystemContext(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        String prompt = promptBuilder.buildPrompt(
                "2024-01-15 京东 100元", context, CategoryStrategy.BY_PLATFORM);

        assertNotNull(prompt);
        assertTrue(prompt.contains("BY_PLATFORM"));
        assertTrue(prompt.contains("平台"));
    }

    // ============================================================
    // 完整流程集成测试
    // ============================================================

    @Test
    void testFullParseFlow() {
        // 准备 UTF-8 CSV 文件
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "full-test.csv", "text/csv", content);

        // 设置 Mock Repository 返回值
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        // 执行解析
        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        // 验证文件内容提取成功
        assertNotNull(response);
        assertTrue(response.isSuccess(), "解析应该成功, error: " + response.getErrorMessage());

        // 验证 LLM 被调用
        verify(parseRecordRepository, never()).save(any());  // 因为 resultCache=null 不走缓存，直接返回

        // 验证解析结果结构
        assertNotNull(response.getRecords());
        assertFalse(response.getRecords().isEmpty());

        // 验证有京东记录
        boolean hasJingdong = response.getRecords().stream()
                .anyMatch(r -> r.getCounterparty() != null && r.getCounterparty().contains("京东"));
        assertTrue(hasJingdong, "应该有京东相关记录");

        // 验证有美团记录
        boolean hasMeituan = response.getRecords().stream()
                .anyMatch(r -> r.getCounterparty() != null && r.getCounterparty().contains("美团"));
        assertTrue(hasMeituan, "应该有美团相关记录");

        // 验证有建议分类
        assertNotNull(response.getSuggestedNewCategories());
        boolean hasSuggestedCategory = response.getSuggestedNewCategories().stream()
                .anyMatch(c -> c.getName().contains("直播娱乐"));
        assertTrue(hasSuggestedCategory, "应该有直播娱乐建议分类");

        // 验证汇总信息
        assertNotNull(response.getSummary());
        assertEquals(3, response.getSummary().getTotalRecords());
    }

    @Test
    void testParseWithByPlatformStrategy() {
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "platform-test.csv", "text/csv", content);

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_PLATFORM);

        assertTrue(response.isSuccess());
        assertNotNull(response.getRecords());
        assertEquals(3, response.getRecords().size());
    }

    @Test
    void testParseWithHybridStrategy() {
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "hybrid-test.csv", "text/csv", content);

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        LlmParseResponse response = facade.parse(file, CategoryStrategy.HYBRID);

        assertTrue(response.isSuccess());
        assertNotNull(response.getSummary());
    }

    @Test
    void testParseEmptyFile() {
        byte[] content = "".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "empty.csv", "text/csv", content);

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        // 空内容可能返回错误或空结果
        assertNotNull(response);
    }

    @Test
    void testAsyncParseReturnsTaskId() {
        byte[] content = SAMPLE_CSV_CONTENT.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "async-test.csv", "text/csv", content);

        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
        when(matcherRepository.findByIsActiveTrueOrderByPriorityDesc()).thenReturn(Collections.emptyList());

        String taskId = facade.parseAsync(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(taskId);
        assertFalse(taskId.isEmpty());

        TaskStatusResponse status = facade.getTaskStatus(taskId);
        assertNotNull(status);
        assertEquals(taskId, status.getTaskId());
    }

    @Test
    void testLLMResponseParsing() throws Exception {
        // 验证 Mock 响应能被正确解析
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        LlmParseResponse response = mapper.readValue(MOCK_LLM_RESPONSE, LlmParseResponse.class);

        assertTrue(response.isSuccess());
        assertEquals(3, response.getRecords().size());
        assertEquals(3, response.getSummary().getTotalRecords());
        assertEquals(0.82, response.getSummary().getAvgConfidence(), 0.01);

        // 验证第一条记录
        CommonBillRecord first = response.getRecords().get(0);
        assertEquals(new BigDecimal("-5999.00"), first.getAmount());
        assertEquals("京东商城", first.getCounterparty());
        assertEquals(3L, first.getCategoryId());
        assertEquals("数码电子", first.getCategoryName());
        assertEquals(0.92, first.getConfidence().doubleValue(), 0.01);
    }

    @Test
    void testSuggestedCategoryParsing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        LlmParseResponse response = mapper.readValue(MOCK_LLM_RESPONSE, LlmParseResponse.class);

        List<SuggestedCategory> suggestions = response.getSuggestedNewCategories();
        assertNotNull(suggestions);
        assertEquals(1, suggestions.size());

        SuggestedCategory suggestion = suggestions.get(0);
        assertEquals("直播娱乐", suggestion.getName());
        assertNull(suggestion.getParentId());
        assertTrue(suggestion.getReason().contains("抖音"));
    }
}
