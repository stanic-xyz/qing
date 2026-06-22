package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.ReconciliationService;
import cn.chenyunlong.qing.service.llm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 TransactionController 在 Web 层会把资源不存在和前置条件失败统一映射为 Result 结构。
 */
class TransactionControllerWebLayerTest {

    private MockMvc mockMvc;

    private TransactionRecordRepository transactionRecordRepository;

    private TransactionService transactionService;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        transactionRecordRepository = mock(TransactionRecordRepository.class);
        ChannelRepository channelRepository = mock(ChannelRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        ReconciliationService reconciliationService = mock(ReconciliationService.class);
        transactionService = mock(TransactionService.class);

        TransactionController controller = new TransactionController(
                transactionRecordRepository,
                channelRepository,
                accountRepository,
                reconciliationService,
                transactionService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(
                        Jackson2ObjectMapperBuilder.json().build()
                ))
                .build();
    }

    /**
     * 验证追踪目标流水不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenTraceTargetDoesNotExist() throws Exception {
        when(transactionRecordRepository.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/finance/transactions/404/trace"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("记录不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证手工新增前置条件失败时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCreateTransactionPreconditionFails() throws Exception {
        when(transactionService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new IllegalStateException("账户已停用，不能新增流水"));

        mockMvc.perform(post("/api/finance/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountId": 1,
                                  "transactionTime": "2026-06-21T10:00:00",
                                  "amount": 12.34
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("账户已停用，不能新增流水"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
