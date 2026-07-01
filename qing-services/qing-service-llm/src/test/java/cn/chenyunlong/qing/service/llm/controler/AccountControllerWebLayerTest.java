package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.AccountImportService;
import cn.chenyunlong.qing.service.llm.service.AccountService;
import cn.chenyunlong.qing.service.llm.service.DedupService;
import cn.chenyunlong.qing.service.llm.service.LinkingService;
import cn.chenyunlong.qing.service.llm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 验证 AccountController 在 Web 层会把资源不存在和前置条件失败统一映射为 Result 结构。
 */
class AccountControllerWebLayerTest {

    private MockMvc mockMvc;

    private AccountRepository accountRepository;

    private TransactionRecordRepository transactionRecordRepository;

    /**
     * 初始化真实 controller 的 standalone MockMvc，并挂载统一异常处理器。
     */
    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionRecordRepository = mock(TransactionRecordRepository.class);
        AccountImportService accountImportService = mock(AccountImportService.class);
        AccountService accountService = mock(AccountService.class);
        DedupService dedupService = mock(DedupService.class);
        LinkingService linkingService = mock(LinkingService.class);
        ChannelRepository channelRepository = mock(ChannelRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        TransactionService transactionService = mock(TransactionService.class);

        AccountController controller = new AccountController(
                accountRepository,
                transactionRecordRepository,
                accountImportService,
                accountService,
                dedupService,
                linkingService,
                channelRepository,
                applicationEventPublisher,
                transactionService
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    /**
     * 验证账户不存在时会输出统一的 404 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenAccountStatisticsTargetDoesNotExist() throws Exception {
        when(accountRepository.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/finance/accounts/404/transaction-count"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("账户不存在"))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }

    /**
     * 验证删除前置条件不满足时会输出统一的 400 错误结构。
     *
     * @throws Exception 测试执行异常
     */
    @Test
    void shouldReturnUnifiedResultWhenDeleteAccountRequiresCascadeFlag() throws Exception {
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRecordRepository.countByAccount(account)).thenReturn(3L);

        mockMvc.perform(delete("/api/finance/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(containsString("该账户下有 3 条关联流水")))
                .andExpect(jsonPath("$.data").value(nullValue()));
    }
}
