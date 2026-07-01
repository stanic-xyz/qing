package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.link.LinkConfig;
import cn.chenyunlong.qing.service.llm.dto.link.LinkReport;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkingServiceTest {

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRecordRepository transactionRepo;

    @Mock
    private SystemConfigService systemConfigService;

    @InjectMocks
    private LinkingService linkingService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        lenient().when(systemConfigService.fillDefaults(any(LinkConfig.class)))
            .thenAnswer(invocation -> {
                LinkConfig c = invocation.getArgument(0);
                if (c == null) {
                    c = new LinkConfig();
                }
                if (c.getTimeToleranceMinutes() == null) c.setTimeToleranceMinutes(5);
                if (c.getMatchMerchant() == null) c.setMatchMerchant(true);
                return c;
            });
    }

    private LocalDateTime now() {
        return LocalDateTime.of(2026, 7, 1, 10, 0, 0);
    }

    private TransactionRecord primary(Long id, String amount, TransactionDirectionTypeEnum dir,
                                       LocalDateTime time, String merchant) {
        TransactionRecord r = new TransactionRecord();
        r.setId(id);
        r.setAmount(new BigDecimal(amount));
        r.setDirectionType(dir);
        r.setTransactionTime(time);
        r.setMerchant(merchant);
        r.setRecordRole(RecordRoleEnum.PRIMARY);
        r.setIsDeleted(false);
        return r;
    }

    private TransactionRecord trace(Long id, String amount, TransactionDirectionTypeEnum dir,
                                     LocalDateTime time, String merchant) {
        TransactionRecord r = new TransactionRecord();
        r.setId(id);
        r.setAmount(new BigDecimal(amount));
        r.setDirectionType(dir);
        r.setTransactionTime(time);
        r.setMerchant(merchant);
        r.setRecordRole(RecordRoleEnum.TRACE);
        r.setIsDeleted(false);
        return r;
    }

    @Nested
    @DisplayName("异常场景")
    class ExceptionScenarios {

        @Test
        @DisplayName("账户不存在时应抛出 NotFoundException")
        void shouldThrowNotFoundWhenAccountMissing() {
            when(accountRepo.findById(99L)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> linkingService.link(99L, new LinkConfig()));

            assertTrue(exception.getMessage().contains("账户不存在"));
        }
    }

    @Nested
    @DisplayName("边界场景")
    class BoundaryScenarios {

        @Test
        @DisplayName("无 TRACE 记录时返回空报告")
        void shouldReturnEmptyWhenNoTraceRecords() {
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, now(), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(0, report.getTotalTraceRecords());
            assertEquals(0, report.getLinkedCount());
        }

        @Test
        @DisplayName("无 PRIMARY 记录时返回空报告")
        void shouldReturnEmptyWhenNoPrimaryRecords() {
            TransactionRecord trace = trace(2L, "100", TransactionDirectionTypeEnum.INCOME, now(), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(trace));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(1, report.getTotalTraceRecords());
            assertEquals(0, report.getLinkedCount());
            assertEquals(1, report.getUnlinkedCount());
        }
    }

    @Nested
    @DisplayName("匹配逻辑")
    class MatchingLogic {

        @Test
        @DisplayName("TRACE 匹配到对应的 PRIMARY 并设置 targetPrimaryRecordId")
        void shouldLinkTraceToPrimary() {
            LocalDateTime base = now();
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord trace = trace(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(2), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary, trace));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(1, report.getLinkedCount());
            assertEquals(0, report.getUnlinkedCount());
            assertEquals(1L, trace.getTargetPrimaryRecordId());
            assertEquals(1L, report.getDetails().get(0).getLinkedPrimaryRecordId());
            assertEquals(2L, report.getDetails().get(0).getTraceRecordId());
            verify(transactionRepo, times(1)).saveAll(List.of(trace));
        }

        @Test
        @DisplayName("金额不同不关联")
        void shouldNotLinkDifferentAmounts() {
            LocalDateTime base = now();
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord trace = trace(2L, "200", TransactionDirectionTypeEnum.INCOME, base, "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary, trace));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(0, report.getLinkedCount());
            assertEquals(1, report.getUnlinkedCount());
            assertNull(trace.getTargetPrimaryRecordId());
        }

        @Test
        @DisplayName("方向不同不关联")
        void shouldNotLinkDifferentDirections() {
            LocalDateTime base = now();
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "转账");
            TransactionRecord trace = trace(2L, "100", TransactionDirectionTypeEnum.EXPENSE, base, "转账");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary, trace));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(0, report.getLinkedCount());
        }

        @Test
        @DisplayName("超过时间窗口不关联")
        void shouldNotLinkOutsideTimeWindow() {
            LocalDateTime base = now();
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord trace = trace(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(10), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary, trace));

            LinkConfig config = new LinkConfig();
            config.setTimeToleranceMinutes(5);
            LinkReport report = linkingService.link(1L, config);

            assertEquals(0, report.getLinkedCount());
        }

        @Test
        @DisplayName("已关联的 TRACE 不重复扫描")
        void shouldSkipAlreadyLinkedTraces() {
            LocalDateTime base = now();
            TransactionRecord primary = primary(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord linked = trace(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");
            linked.setTargetPrimaryRecordId(1L);

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(primary, linked));

            LinkReport report = linkingService.link(1L, new LinkConfig());

            assertEquals(0, report.getLinkedCount());
            assertEquals(0, report.getTotalTraceRecords());
        }
    }
}
