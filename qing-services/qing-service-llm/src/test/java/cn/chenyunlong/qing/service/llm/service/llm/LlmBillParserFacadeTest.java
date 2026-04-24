package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.dto.parser.LlmParseResponse;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Counterparty;
import cn.chenyunlong.qing.service.llm.enums.CategoryStrategy;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.LlmParseDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * LLM 账单解析 Facade 集成测试
 */
@ExtendWith(MockitoExtension.class)
class LlmBillParserFacadeTest {

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

    private LlmBillParserFacade facade;
    private LlmFileContentExtractor fileContentExtractor;
    private LlmParseContextLoader contextLoader;
    private LlmParseResultCache resultCache;
    private LlmParseTaskService taskService;
    private MockLlmParser mockLlmParser;
    private LlmPromptBuilder promptBuilder;

    @BeforeEach
    void setUp() {
        fileContentExtractor = new LlmFileContentExtractor();
        contextLoader = new LlmParseContextLoader(
                categoryRepository, accountRepository,
                counterpartyRepository, null
        );
        resultCache = mock(LlmParseResultCache.class);
        taskService = new LlmParseTaskService();
        mockLlmParser = new MockLlmParser();
        promptBuilder = new LlmPromptBuilder();

        facade = new LlmBillParserFacade(
                fileContentExtractor, contextLoader, resultCache,
                taskService, mockLlmParser, promptBuilder,
                parseRecordRepository, detailRepository,
                categoryRepository, accountRepository, counterpartyRepository
        );
    }

    @Test
    void testGetTaskStatus() {
        String taskId = taskService.submitTask("TEST");

        var status = facade.getTaskStatus(taskId);

        assertNotNull(status);
        assertEquals(taskId, status.getTaskId());
        assertEquals("PENDING", status.getStatus());
    }

    @Test
    void testGetTaskStatusNotFound() {
        var status = facade.getTaskStatus("nonexistent-task-id");

        assertNull(status);
    }

    @Test
    void testParseRecordNotFound() {
        when(parseRecordRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        var record = facade.getParseRecord(999L);

        assertTrue(record.isEmpty());
    }

    @Test
    void testParseRecordByTaskIdNotFound() {
        when(parseRecordRepository.findByTaskId("nonexistent")).thenReturn(java.util.Optional.empty());

        var record = facade.getParseRecordByTaskId("nonexistent");

        assertTrue(record.isEmpty());
    }

    @Test
    void testParseWithMockParser() throws Exception {
        // 验证 mock parser 能处理真实 prompt 并返回有效 JSON
        String prompt = "【原始账单文本】\n2024-01-01 京东购物 123.45元\n【系统上下文】\n分类列表";

        String result = mockLlmParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("success"));
        assertTrue(result.contains("\"records\""));
        assertTrue(result.contains("\"summary\""));
    }

    @Test
    void testParseWithByPlatformStrategy() throws Exception {
        String prompt = "【原始账单文本】\n2024-01-01 京东购物 123.45元\n【系统上下文】";

        String result = mockLlmParser.parse(prompt, CategoryStrategy.BY_PLATFORM);

        assertNotNull(result);
        assertTrue(result.contains("success"));
        assertTrue(result.contains("\"records\""));
        assertTrue(result.contains("京东"));
    }

    @Test
    void testParseWithHybridStrategy() throws Exception {
        String prompt = "【原始账单文本】\n2024-01-01 京东购物 123.45元\n【系统上下文】";

        String result = mockLlmParser.parse(prompt, CategoryStrategy.HYBRID);

        assertNotNull(result);
        assertTrue(result.contains("success"));
        assertTrue(result.contains("\"records\""));
    }

    @Test
    void testTaskServiceSubmit() {
        String taskId = taskService.submitTask("LLM_PARSE");

        assertNotNull(taskId);
        assertTrue(taskId.length() > 0);

        var status = taskService.getStatus(taskId);
        assertNotNull(status);
        assertEquals("PENDING", status.getStatus());
    }

    @Test
    void testTaskServiceUpdateProgress() {
        String taskId = taskService.submitTask("LLM_PARSE");
        taskService.updateProgress(taskId, 50);

        var status = taskService.getStatus(taskId);
        assertEquals(50, status.getProgress());
        assertEquals("RUNNING", status.getStatus());
    }

    @Test
    void testTaskServiceComplete() {
        String taskId = taskService.submitTask("LLM_PARSE");
        taskService.updateProgress(taskId, 100);

        var status = taskService.getStatus(taskId);
        assertEquals(100, status.getProgress());
        assertEquals("COMPLETED", status.getStatus());
    }

    @Test
    void testTaskServiceCancel() {
        String taskId = taskService.submitTask("LLM_PARSE");
        boolean cancelled = taskService.cancel(taskId);

        assertTrue(cancelled);
        var status = taskService.getStatus(taskId);
        assertEquals("CANCELLED", status.getStatus());
    }

    @Test
    void testMockParserWithEmptyText() throws Exception {
        String prompt = "【原始账单文本】\n\n【系统上下文】";

        String result = mockLlmParser.parse(prompt, CategoryStrategy.BY_CONSUMPTION_TYPE);

        assertNotNull(result);
        assertTrue(result.contains("success"));
        // 空文本仍应返回有效 JSON 结构
        assertTrue(result.contains("\"records\""));
    }
}
