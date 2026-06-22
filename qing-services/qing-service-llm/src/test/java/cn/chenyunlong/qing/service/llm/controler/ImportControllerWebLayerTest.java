package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiFileService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiImportService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiPreviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 ImportController 在 Web 层会把资源不存在和前置条件失败统一映射为 Result 结构。
 */
class ImportControllerWebLayerTest {

    private MockMvc mockMvc;

    private QianjiFileService qianjiFileService;

    private QianjiImportService qianjiImportService;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        qianjiImportService = mock(QianjiImportService.class);
        qianjiFileService = mock(QianjiFileService.class);
        QianjiPreviewService qianjiPreviewService = mock(QianjiPreviewService.class);

        ImportController controller = new ImportController(
                qianjiImportService,
                qianjiFileService,
                qianjiPreviewService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    /**
     * 验证文件不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenUploadFileRecordDoesNotExist() throws Exception {
        when(qianjiFileService.getFileInfo(404L)).thenThrow(new NotFoundException("上传文件记录不存在: 404"));

        mockMvc.perform(get("/api/finance/import/qianji/files/404"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("上传文件记录不存在: 404"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证导入执行前置条件失败时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenExecuteRecordsPreconditionFails() throws Exception {
        when(qianjiImportService.executeRecords(any()))
                .thenThrow(new BusinessException("请先完成账单预览后再执行导入"));

        mockMvc.perform(post("/api/finance/import/qianji/executeRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceFile": "test.csv"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("请先完成账单预览后再执行导入"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
