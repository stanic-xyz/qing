package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import cn.chenyunlong.qing.service.llm.service.MatcherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 MatcherController 在 Web 层会把资源不存在统一映射为 Result 结构。
 */
class MatcherControllerWebLayerTest {

    private MockMvc mockMvc;

    private TransactionRecordRepository transactionRecordRepository;

    private UnifiedDraftRecordRepository unifiedDraftRecordRepository;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        TransactionMatcherRepository transactionMatcherRepository = mock(TransactionMatcherRepository.class);
        transactionRecordRepository = mock(TransactionRecordRepository.class);
        unifiedDraftRecordRepository = mock(UnifiedDraftRecordRepository.class);
        MatcherService matcherService = mock(MatcherService.class);

        MatcherController controller = new MatcherController(
                transactionMatcherRepository,
                transactionRecordRepository,
                unifiedDraftRecordRepository,
                matcherService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper()))
                .build();
    }

    /**
     * 验证目标流水和草稿都不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenMatcherTargetDoesNotExist() throws Exception {
        when(transactionRecordRepository.findById(404L)).thenReturn(Optional.empty());
        when(unifiedDraftRecordRepository.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/finance/matchers/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactionId": 404,
                                  "matcher": {}
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("未找到指定的交易流水"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
