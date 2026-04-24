package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.LlmParseResponse;
import cn.chenyunlong.qing.service.llm.dto.parser.TaskStatusResponse;
import cn.chenyunlong.qing.service.llm.dto.parser.CommonBillRecord;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * LlmBillParserFacade 单元测试
 *
 * <p>使用 Mockito Mock {@link LlmParserService} 接口，
 * 不依赖真实 LLM 调用，专注于测试 Facade 的编排逻辑。</p>
 */
@ExtendWith(MockitoExtension.class)
class LlmBillParserFacadeTest {

    // ============================================================
    // Mock 依赖
    // ============================================================

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CounterpartyRepository counterpartyRepository;
    @Mock
    private LlmParseRecordRepository parseRecordRepository;
    @Mock
    private LlmParseDetailRepository detailRepository;
    @Mock
    private LlmParserService mockLlmParser;  // Mock 接口，而非具体类

    // ============================================================
    // 真实组件
    // ============================================================

    private LlmFileContentExtractor fileContentExtractor;
    private LlmParseContextLoader contextLoader;
    private LlmParseResultCache resultCache;
    private LlmParseTaskService taskService;
    private LlmPromptBuilder promptBuilder;

    // ============================================================
    // 被测系统
    // ============================================================

    private LlmBillParserFacade facade;

    // ============================================================
    // 测试数据
    // ============================================================

    private static final String SAMPLE_BILL = """
            2024-01-15 12:30:00  京东商城-笔记本  -5999.00元
            2024-01-15 18:30:00  美团外卖-麦当劳   -45.50元
            """;

    private static final String MOCK_RESPONSE_JSON = """
            {
              "success": true,
              "taskId": "unit-test-task",
              "records": [
                {
                  "amount": -5999.00,
                  "transactionType": "EXPENSE",
                  "transactionTime": "2024-01-15T12:30:00",
                  "counterparty": "京东商城",
                  "description": "京东商城-笔记本",
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
                }
              ],
              "summary": {
                "totalRecords": 2,
                "successCount": 2,
                "failedCount": 0,
                "needReviewCount": 0,
                "avgConfidence": 0.90,
                "inputTokens": 100,
                "outputTokens": 60,
                "estimatedCost": 0.002,
                "platformSources": ["京东","美团"],
                "consumptionTypes": ["数码电子","餐饮美食"]
              },
              "suggestedNewCategories": [],
              "suggestedNewAccounts": [],
              "unmatchedRecords": []
            }
            """;

    @BeforeEach
    void setUp() {
        fileContentExtractor = new LlmFileContentExtractor();
        contextLoader = new LlmParseContextLoader(
                categoryRepository, accountRepository,
                counterpartyRepository, null
        );
        resultCache = mock(LlmParseResultCache.class);
        taskService = new LlmParseTaskService();
        promptBuilder = new LlmPromptBuilder();

        facade = new LlmBillParserFacade(
                fileContentExtractor, contextLoader, resultCache,
                taskService, mockLlmParser, promptBuilder,
                parseRecordRepository, detailRepository,
                categoryRepository, accountRepository, counterpartyRepository
        );
        facade.init();

        // Mock Repository 默认返回值
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(Collections.emptyList());
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());
        when(counterpartyRepository.findAll()).thenReturn(Collections.emptyList());
    }

    // ============================================================
    // 文件提取测试
    // ============================================================

    @Test
    void testFileExtractionFromText() {
        byte[] content = SAMPLE_BILL.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", content);

        String text = fileContentExtractor.extract(content, "test.txt");

        assertNotNull(text);
        assertTrue(text.contains("京东"));
        assertTrue(text.contains("美团"));
    }

    @Test
    void testFileExtractionFromCsv() {
        String csv = "时间,描述,金额\n2024-01-15,京东,-100";
        byte[] content = csv.getBytes(StandardCharsets.UTF_8);

        String text = fileContentExtractor.extract(content, "test.csv");

        assertNotNull(text);
        assertTrue(text.contains("京东"));
    }

    // ============================================================
    // Facade 核心逻辑测试
    // ============================================================

    @Test
    void testParseCallsLLM() throws Exception {
        // 配置 Mock LLM 返回
        when(mockLlmParser.parse(anyString(), any(CategoryStrategy.class)))
                .thenReturn(MOCK_RESPONSE_JSON);
        when(resultCache.hasValid(anyString())).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", SAMPLE_BILL.getBytes(StandardCharsets.UTF_8));

        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(2, response.getRecords().size());
        assertEquals("京东商城", response.getRecords().get(0).getCounterparty());

        // 验证 LLM 被调用
        verify(mockLlmParser, times(1))
                .parse(anyString(), eq(CategoryStrategy.BY_CONSUMPTION_TYPE));
    }

    @Test
    void testParseSkipsCacheWhenHasValid() throws Exception {
        LlmParseResponse cachedResponse = new LlmParseResponse();
        cachedResponse.setSuccess(true);

        when(resultCache.hasValid(anyString())).thenReturn(true);
        when(resultCache.get(anyString())).thenReturn(cachedResponse);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", SAMPLE_BILL.getBytes(StandardCharsets.UTF_8));

        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertTrue(response.isSuccess());
        // LLM 不应该被调用
        verify(mockLlmParser, never()).parse(anyString(), any());
        verify(resultCache, times(1)).get(anyString());
    }

    @Test
    void testParseReturnsErrorOnEmptyContent() {
        when(resultCache.hasValid(anyString())).thenReturn(false);

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.csv", "text/csv", "".getBytes(StandardCharsets.UTF_8));

        LlmParseResponse response = facade.parse(emptyFile, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("无法从文件中提取"));
    }

    @Test
    void testParseSupportsByPlatformStrategy() throws Exception {
        when(mockLlmParser.parse(anyString(), any(CategoryStrategy.class)))
                .thenReturn(MOCK_RESPONSE_JSON);
        when(resultCache.hasValid(anyString())).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", SAMPLE_BILL.getBytes(StandardCharsets.UTF_8));

        LlmParseResponse response = facade.parse(file, CategoryStrategy.BY_PLATFORM);

        assertTrue(response.isSuccess());
        verify(mockLlmParser, times(1))
                .parse(anyString(), eq(CategoryStrategy.BY_PLATFORM));
    }

    @Test
    void testParseSupportsHybridStrategy() throws Exception {
        when(mockLlmParser.parse(anyString(), any(CategoryStrategy.class)))
                .thenReturn(MOCK_RESPONSE_JSON);
        when(resultCache.hasValid(anyString())).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", SAMPLE_BILL.getBytes(StandardCharsets.UTF_8));

        LlmParseResponse response = facade.parse(file, CategoryStrategy.HYBRID);

        assertTrue(response.isSuccess());
        verify(mockLlmParser, times(1))
                .parse(anyString(), eq(CategoryStrategy.HYBRID));
    }

    // ============================================================
    // 异步任务测试
    // ============================================================

    @Test
    void testAsyncParseReturnsTaskId() throws Exception {
        when(mockLlmParser.parse(anyString(), any(CategoryStrategy.class)))
                .thenReturn(MOCK_RESPONSE_JSON);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", SAMPLE_BILL.getBytes(StandardCharsets.UTF_8));

        String taskId = facade.parseAsync(file, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(taskId);
        assertFalse(taskId.isEmpty());

        // 任务状态应为 PENDING（异步任务刚提交）
        TaskStatusResponse status = facade.getTaskStatus(taskId);
        assertNotNull(status);
        assertEquals(taskId, status.getTaskId());
    }

    @Test
    void testGetTaskStatusReturnsNullForUnknownTask() {
        TaskStatusResponse status = facade.getTaskStatus("unknown-task-id");
        assertNull(status);
    }

    @Test
    void testGetParseRecordReturnsEmptyForUnknown() {
        when(parseRecordRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        var record = facade.getParseRecord(999L);
        assertTrue(record.isEmpty());
    }

    // ============================================================
    // 任务服务测试
    // ============================================================

    @Test
    void testTaskServiceSubmitAndGet() {
        String taskId = taskService.submitTask("TEST");

        assertNotNull(taskId);

        var status = taskService.getStatus(taskId);
        assertNotNull(status);
        assertEquals("PENDING", status.getStatus());
        assertEquals(0, status.getProgress());
    }

    @Test
    void testTaskServiceUpdateProgress() {
        String taskId = taskService.submitTask("TEST");
        taskService.updateProgress(taskId, 50);

        var status = taskService.getStatus(taskId);
        assertEquals("RUNNING", status.getStatus());
        assertEquals(50, status.getProgress());
    }

    @Test
    void testTaskServiceComplete() {
        String taskId = taskService.submitTask("TEST");
        taskService.complete(taskId);

        var status = taskService.getStatus(taskId);
        assertEquals("COMPLETED", status.getStatus());
        assertEquals(100, status.getProgress());
    }

    @Test
    void testTaskServiceFail() {
        String taskId = taskService.submitTask("TEST");
        taskService.fail(taskId, "Test error");

        var status = taskService.getStatus(taskId);
        assertEquals("FAILED", status.getStatus());
        assertEquals("Test error", status.getErrorMessage());
    }

    @Test
    void testTaskServiceCancel() {
        String taskId = taskService.submitTask("TEST");
        boolean cancelled = taskService.cancel(taskId);

        assertTrue(cancelled);
        var status = taskService.getStatus(taskId);
        assertEquals("CANCELLED", status.getStatus());
    }

    @Test
    void testTaskServiceCancelUnknownTask() {
        boolean cancelled = taskService.cancel("nonexistent");
        assertFalse(cancelled);
    }
}
