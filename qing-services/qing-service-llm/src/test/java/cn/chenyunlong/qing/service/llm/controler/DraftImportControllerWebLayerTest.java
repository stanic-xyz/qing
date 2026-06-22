package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.service.DraftBatchService;
import cn.chenyunlong.qing.service.llm.service.DraftCommitService;
import cn.chenyunlong.qing.service.llm.service.DraftRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证真实 DraftImportController 在 Web 层的异常输出会遵循统一约束。
 */
class DraftImportControllerWebLayerTest {

    private MockMvc mockMvc;

    private DraftCommitService draftCommitService;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载全局异常处理器。
     */
    @BeforeEach
    void setUp() {
        DraftBatchService draftBatchService = mock(DraftBatchService.class);
        DraftRecordService draftRecordService = mock(DraftRecordService.class);
        draftCommitService = mock(DraftCommitService.class);

        DraftImportController controller = new DraftImportController(
                draftBatchService,
                draftRecordService,
                draftCommitService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    /**
     * 验证非法状态异常会交给全局异常处理器输出统一结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCommitThrowsIllegalStateException() throws Exception {
        when(draftCommitService.commit(1L)).thenThrow(new IllegalStateException("批次状态不允许提交"));

        mockMvc.perform(post("/api/import/draft/batches/1/commit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("批次状态不允许提交"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证资源不存在异常会交给全局异常处理器输出统一结果。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCommitThrowsNotFoundException() throws Exception {
        when(draftCommitService.commit(1L)).thenThrow(new NotFoundException("上传文件记录不存在: 1"));

        mockMvc.perform(post("/api/import/draft/batches/1/commit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("上传文件记录不存在: 1"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证未知异常会交给全局异常处理器兜底，且不会泄露底层异常消息。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldHideUnknownExceptionMessageWhenCommitThrowsRuntimeException() throws Exception {
        when(draftCommitService.commit(1L)).thenThrow(new RuntimeException("底层数据库连接串泄露"));

        mockMvc.perform(post("/api/import/draft/batches/1/commit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统繁忙，请稍后重试"))
                .andExpect(jsonPath("$.message").value(not(containsString("底层数据库连接串泄露"))))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
