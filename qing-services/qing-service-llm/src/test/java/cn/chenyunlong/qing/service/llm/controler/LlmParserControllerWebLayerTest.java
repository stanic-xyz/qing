package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.parser.LlmParseResponse;
import cn.chenyunlong.qing.service.llm.service.llm.LlmBillParserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 LlmParserController 预览失败时会交由统一异常处理器输出标准错误结构。
 */
class LlmParserControllerWebLayerTest {

    private MockMvc mockMvc;

    private LlmBillParserFacade parserFacade;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        parserFacade = mock(LlmBillParserFacade.class);
        LlmParserController controller = new LlmParserController(parserFacade);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    /**
     * 验证预览解析失败时会输出统一的 400 错误结构，并保留业务消息。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenPreviewParsingFails() throws Exception {
        when(parserFacade.parse(any(), eq(cn.chenyunlong.qing.service.llm.enums.CategoryStrategy.BY_CONSUMPTION_TYPE)))
                .thenReturn(LlmParseResponse.error("不支持的账单格式"));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "bill.csv",
                "text/csv",
                "header".getBytes()
        );

        mockMvc.perform(multipart("/api/llm/parser/preview").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("不支持的账单格式"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
