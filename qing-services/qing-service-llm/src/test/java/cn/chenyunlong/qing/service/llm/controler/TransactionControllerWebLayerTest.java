package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.ReconciliationService;
import cn.chenyunlong.qing.service.llm.service.TransactionQueryService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        ReconciliationService reconciliationService = mock(ReconciliationService.class);
        TransactionQueryService transactionQueryService = mock(TransactionQueryService.class);
        transactionService = mock(TransactionService.class);

        TransactionController controller = new TransactionController(
                transactionRecordRepository,
                reconciliationService,
                transactionService,
                transactionQueryService
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
                                  "amount": 12.34,
                                  "directionType": "INCOME"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("账户已停用，不能新增流水"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证手工新增缺少显式方向字段时会输出统一的参数校验错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCreateTransactionDirectionTypeMissing() throws Exception {
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
                .andExpect(jsonPath("$.message").value("directionType: directionType不能为空"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证负金额支出请求不会在 Web 层被旧的 Bean Validation 规则拦截。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldAllowNegativeAmountWhenCreateTransactionDirectionMatches() throws Exception {
        when(transactionService.create(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new cn.chenyunlong.qing.service.llm.entity.TransactionRecord());

        mockMvc.perform(post("/api/finance/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountId": 1,
                                  "transactionTime": "2026-06-21T10:00:00",
                                  "amount": -12.34,
                                  "directionType": "EXPENSE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"));

        verify(transactionService).create(org.mockito.ArgumentMatchers.any());
    }

    /**
     * 验证服务层返回分类方向冲突时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCreateTransactionCategoryDirectionMismatched() throws Exception {
        when(transactionService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new BusinessException("分类方向与交易方向不匹配，分类[餐饮]要求支出方向"));

        mockMvc.perform(post("/api/finance/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountId": 1,
                                  "transactionTime": "2026-06-21T10:00:00",
                                  "amount": 12.34,
                                  "directionType": "INCOME",
                                  "categoryId": 40
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("分类方向与交易方向不匹配，分类[餐饮]要求支出方向"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证服务层拒绝旧转账语义时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenCreateTransactionUsesTransferSemantics() throws Exception {
        when(transactionService.create(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new BusinessException("当前手工交易接口不支持TRANSFER语义，请使用独立转账接口"));

        mockMvc.perform(post("/api/finance/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountId": 1,
                                  "transactionTime": "2026-06-21T10:00:00",
                                  "amount": 12.34,
                                  "directionType": "INCOME",
                                  "transactionType": "TRANSFER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("当前手工交易接口不支持TRANSFER语义，请使用独立转账接口"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证更新时金额符号与方向不一致会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenUpdateTransactionAmountDirectionMismatched() throws Exception {
        when(transactionService.update(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any()))
                .thenThrow(new BusinessException("amount与directionType不匹配，收入使用正金额，支出使用负金额"));

        mockMvc.perform(put("/api/finance/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 12.34,
                                  "directionType": "EXPENSE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("amount与directionType不匹配，收入使用正金额，支出使用负金额"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
