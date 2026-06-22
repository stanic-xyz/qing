package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 TagController 在 Web 层会把资源不存在和前置条件失败统一映射为 Result 结构。
 */
class TagControllerWebLayerTest {

    private MockMvc mockMvc;

    private TagService tagService;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        tagService = mock(TagService.class);
        TagController controller = new TagController(tagService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    /**
     * 验证标签不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenTagDoesNotExist() throws Exception {
        when(tagService.getById(404L)).thenThrow(new NotFoundException("标签不存在"));

        mockMvc.perform(get("/api/finance/tags/404"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("标签不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证标签名称为空时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCreateTagNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/finance/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "   ",
                                  "color": "#FF0000"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("标签名称不能为空"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
