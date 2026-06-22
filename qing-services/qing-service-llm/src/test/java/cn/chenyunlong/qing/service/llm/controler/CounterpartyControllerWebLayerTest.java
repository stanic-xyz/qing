package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.CounterpartyRepository;
import cn.chenyunlong.qing.service.llm.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 CounterpartyController 在 Web 层会把资源不存在统一映射为 Result 结构。
 */
class CounterpartyControllerWebLayerTest {

    private MockMvc mockMvc;

    private CounterpartyRepository counterpartyRepository;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        counterpartyRepository = mock(CounterpartyRepository.class);
        CategoryService categoryService = mock(CategoryService.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        CounterpartyController controller = new CounterpartyController();
        ReflectionTestUtils.setField(controller, "counterpartyRepository", counterpartyRepository);
        ReflectionTestUtils.setField(controller, "categoryService", categoryService);
        ReflectionTestUtils.setField(controller, "categoryRepository", categoryRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    /**
     * 验证交易对手不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCounterpartyDoesNotExist() throws Exception {
        when(counterpartyRepository.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/finance/counterparties/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "测试交易对手"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("交易对手不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
