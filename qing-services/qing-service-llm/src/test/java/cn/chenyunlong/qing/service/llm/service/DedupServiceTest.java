package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupConfig;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupReport;
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

import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DedupServiceTest {

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRecordRepository transactionRepo;

    @Mock
    private SystemConfigService systemConfigService;

    @InjectMocks
    private DedupService dedupService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setInitialBalance(BigDecimal.ZERO);
        // 模拟系统配置填充：null 字段使用默认值
        lenient().when(systemConfigService.fillDefaults(any(DedupConfig.class)))
            .thenAnswer(invocation -> {
                DedupConfig c = invocation.getArgument(0);
                if (c == null) {
                    c = new DedupConfig();
                }
                if (c.getTimeToleranceMinutes() == null) c.setTimeToleranceMinutes(5);
                if (c.getMatchMerchant() == null) c.setMatchMerchant(true);
                if (c.getMatchDetail() == null) c.setMatchDetail(false);
                return c;
            });
    }

    @Nested
    @DisplayName("异常场景")
    class ExceptionScenarios {

        @Test
        @DisplayName("账户不存在时应抛出 NotFoundException")
        void shouldThrowNotFoundWhenAccountMissing() {
            when(accountRepo.findById(99L)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> dedupService.deduplicate(99L, new DedupConfig()));

            assertTrue(exception.getMessage().contains("账户不存在"));
        }

        @Test
        @DisplayName("null 配置时应使用默认配置")
        void shouldHandleNullConfig() {
            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of());

            DedupReport report = dedupService.deduplicate(1L, null);

            assertNotNull(report);
            assertEquals(0, report.getTotalRecords());
        }
    }

    @Nested
    @DisplayName("边界场景")
    class BoundaryScenarios {

        @Test
        @DisplayName("账户下无记录时返回空报告")
        void shouldReturnEmptyReportWhenNoRecords() {
            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of());

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(0, report.getTotalRecords());
            assertEquals(0, report.getDuplicateGroups());
            assertEquals(0, report.getMarkedCount());
        }

        @Test
        @DisplayName("只有 1 条记录时无需去重")
        void shouldSkipWhenOnlyOneRecord() {
            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account))
                    .thenReturn(List.of(createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, now())));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1, report.getTotalRecords());
            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("软删除的记录不参与去重")
        void shouldSkipSoftDeletedRecords() {
            TransactionRecord deleted = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, now());
            deleted.setIsDeleted(true);
            TransactionRecord normal = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, now());

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(deleted, normal));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1, report.getTotalRecords());
            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("已标记 duplicateOf 的记录不参与去重")
        void shouldSkipAlreadyMarkedRecords() {
            TransactionRecord marked = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, now());
            marked.setDuplicateOf(999L);
            TransactionRecord normal = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, now());

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(marked, normal));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1, report.getTotalRecords());
            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("TRACE 记录不参与 PRIMARY 去重")
        void shouldSkipTraceRecordsInDedup() {
            TransactionRecord trace = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, now());
            trace.setRecordRole(RecordRoleEnum.TRACE);
            TransactionRecord primary = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, now());
            primary.setRecordRole(RecordRoleEnum.PRIMARY);

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(trace, primary));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1, report.getTotalRecords());
            assertEquals(0, report.getDuplicateGroups());
        }
    }

    @Nested
    @DisplayName("匹配逻辑")
    class MatchingLogic {

        @Test
        @DisplayName("默认配置下按金额+时间+方向+商户匹配，识别重复组")
        void shouldDetectDuplicatesWithDefaultConfig() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(2), "星巴克");
            TransactionRecord r3 = createRecord(3L, "200", TransactionDirectionTypeEnum.EXPENSE, base, "美团");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2, r3));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(3, report.getTotalRecords());
            assertEquals(1, report.getDuplicateGroups());
            assertEquals(1, report.getMarkedCount());
            assertEquals(1L, report.getKeptCount());
            assertEquals(1L, report.getDetails().get(0).getKeptRecordId());
            assertEquals(List.of(2L), report.getDetails().get(0).getDuplicateRecordIds());
            assertTrue(report.getDetails().get(0).getMatchedBy().contains("merchant"));
        }

        @Test
        @DisplayName("matchMerchant=false 时不匹配商户，更多记录被归为一组")
        void shouldNotMatchMerchantWhenDisabled() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "50", TransactionDirectionTypeEnum.EXPENSE, base, "肯德基");
            TransactionRecord r2 = createRecord(2L, "50", TransactionDirectionTypeEnum.EXPENSE, base.plusMinutes(1), "麦当劳");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setMatchMerchant(false);
            DedupReport report = dedupService.deduplicate(1L, config);

            assertEquals(1, report.getDuplicateGroups());
            assertEquals(1, report.getMarkedCount());
            List<String> matchedBy = report.getDetails().get(0).getMatchedBy();
            assertFalse(matchedBy.contains("merchant"));
        }

        @Test
        @DisplayName("matchDetail=true 时详情不同则不匹配")
        void shouldRespectDetailMatchWhenEnabled() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            r1.setDetail("拿铁");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");
            r2.setDetail("美式");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setMatchDetail(true);
            DedupReport report = dedupService.deduplicate(1L, config);

            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("matchDetail=false 时忽略详情差异，仍视为重复")
        void shouldIgnoreDetailWhenDisabled() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            r1.setDetail("拿铁");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");
            r2.setDetail("美式");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setMatchDetail(false);
            DedupReport report = dedupService.deduplicate(1L, config);

            assertEquals(1, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("金额不同不匹配")
        void shouldNotMatchDifferentAmounts() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "200", TransactionDirectionTypeEnum.INCOME, base, "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("方向不同不匹配")
        void shouldNotMatchDifferentDirections() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "转账");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.EXPENSE, base, "转账");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("超过时间容忍窗口不匹配")
        void shouldNotMatchOutsideTimeWindow() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(10), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setTimeToleranceMinutes(5);
            DedupReport report = dedupService.deduplicate(1L, config);

            assertEquals(0, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("自定义时间窗口内匹配成功")
        void shouldMatchWithinCustomTimeWindow() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(10), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setTimeToleranceMinutes(10);
            DedupReport report = dedupService.deduplicate(1L, config);

            assertEquals(1, report.getDuplicateGroups());
        }

        @Test
        @DisplayName("保留最早创建的记录（按 createdAt）")
        void shouldKeepEarliestCreatedRecord() {
            LocalDateTime base = now();
            TransactionRecord early = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            early.setCreatedAt(base.minusHours(2));
            TransactionRecord late = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");
            late.setCreatedAt(base);

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(late, early));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1L, report.getDetails().get(0).getKeptRecordId());
            assertEquals(List.of(2L), report.getDetails().get(0).getDuplicateRecordIds());
        }
    }

    @Nested
    @DisplayName("报告准确性")
    class ReportAccuracy {

        @Test
        @DisplayName("多组重复时分别报告")
        void shouldReportMultipleDuplicateGroups() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "10", TransactionDirectionTypeEnum.INCOME, base, "早餐");
            TransactionRecord r2 = createRecord(2L, "10", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "早餐");
            TransactionRecord r3 = createRecord(3L, "20", TransactionDirectionTypeEnum.EXPENSE, base, "打车");
            TransactionRecord r4 = createRecord(4L, "20", TransactionDirectionTypeEnum.EXPENSE, base.plusMinutes(2), "打车");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2, r3, r4));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(2, report.getDuplicateGroups());
            assertEquals(2, report.getMarkedCount());
            assertEquals(2, report.getKeptCount());
            assertEquals(2, report.getDetails().size());
        }

        @Test
        @DisplayName("matchedBy 包含实际命中的字段")
        void shouldReportCorrectMatchedByFields() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            r1.setDetail("拿铁");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");
            r2.setDetail("拿铁");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            DedupConfig config = new DedupConfig();
            config.setMatchDetail(true);
            DedupReport report = dedupService.deduplicate(1L, config);

            List<String> matchedBy = report.getDetails().get(0).getMatchedBy();
            assertTrue(matchedBy.contains("amount"));
            assertTrue(matchedBy.contains("transactionTime"));
            assertTrue(matchedBy.contains("directionType"));
            assertTrue(matchedBy.contains("merchant"));
            assertTrue(matchedBy.contains("detail"));
        }

        @Test
        @DisplayName("被保留的记录不应出现在 duplicateRecordIds 中")
        void keptRecordShouldNotBeListedAsDuplicate() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(2), "星巴克");
            TransactionRecord r3 = createRecord(3L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(4), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2, r3));

            DedupReport report = dedupService.deduplicate(1L, new DedupConfig());

            List<Long> dupIds = report.getDetails().get(0).getDuplicateRecordIds();
            assertFalse(dupIds.contains(1L));
            assertTrue(dupIds.containsAll(List.of(2L, 3L)));
        }
    }

    @Nested
    @DisplayName("持久化验证")
    class PersistenceVerification {

        @Test
        @DisplayName("重复记录被标记 duplicateOf 并保存")
        void shouldMarkAndSaveDuplicates() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(2), "星巴克");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            dedupService.deduplicate(1L, new DedupConfig());

            assertEquals(1L, r2.getDuplicateOf());
            assertNull(r1.getDuplicateOf());
            verify(transactionRepo, times(1)).saveAll(List.of(r2));
        }

        @Test
        @DisplayName("无重复时不调用 saveAll")
        void shouldNotSaveWhenNoDuplicates() {
            LocalDateTime base = now();
            TransactionRecord r1 = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord r2 = createRecord(2L, "200", TransactionDirectionTypeEnum.EXPENSE, base, "美团");

            when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(r1, r2));

            dedupService.deduplicate(1L, new DedupConfig());

            verify(transactionRepo, never()).saveAll(any());
        }
    }

    @Nested
    @DisplayName("findDuplicate 单条检测")
    class FindDuplicate {

        @Test
        @DisplayName("匹配到重复时返回匹配的记录")
        void shouldReturnMatchedRecordWhenDuplicateFound() {
            LocalDateTime base = now();
            TransactionRecord existing = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord candidate = createRecord(null, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(2), "星巴克");

            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(existing));

            TransactionRecord result = dedupService.findDuplicate(account, candidate, new DedupConfig());

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }

        @Test
        @DisplayName("无匹配时返回 null")
        void shouldReturnNullWhenNoDuplicate() {
            LocalDateTime base = now();
            TransactionRecord existing = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            TransactionRecord candidate = createRecord(null, "200", TransactionDirectionTypeEnum.INCOME, base, "星巴克");

            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(existing));

            TransactionRecord result = dedupService.findDuplicate(account, candidate, new DedupConfig());

            assertNull(result);
        }

        @Test
        @DisplayName("TRACE 记录不参与 PRIMARY 去重检查")
        void shouldSkipTraceRecordsInFindDuplicate() {
            LocalDateTime base = now();
            TransactionRecord trace = createRecord(1L, "100", TransactionDirectionTypeEnum.INCOME, base, "星巴克");
            trace.setRecordRole(RecordRoleEnum.TRACE);
            TransactionRecord candidate = createRecord(null, "100", TransactionDirectionTypeEnum.INCOME, base.plusMinutes(1), "星巴克");

            when(transactionRepo.findAllByAccount(account)).thenReturn(List.of(trace));

            TransactionRecord result = dedupService.findDuplicate(account, candidate, new DedupConfig());

            assertNull(result);
        }
    }

    // ========== 辅助方法 ==========

    private LocalDateTime now() {
        return LocalDateTime.of(2026, 7, 1, 10, 0, 0);
    }

    private TransactionRecord createRecord(Long id, String amount,
                                            TransactionDirectionTypeEnum direction,
                                            LocalDateTime transactionTime) {
        return createRecord(id, amount, direction, transactionTime, null);
    }

    private TransactionRecord createRecord(Long id, String amount,
                                            TransactionDirectionTypeEnum direction,
                                            LocalDateTime transactionTime,
                                            String merchant) {
        TransactionRecord r = new TransactionRecord();
        r.setId(id);
        r.setAmount(new BigDecimal(amount));
        r.setDirectionType(direction);
        r.setTransactionTime(transactionTime);
        r.setMerchant(merchant);
        r.setIsDeleted(false);
        r.setCreatedAt(transactionTime);
        return r;
    }
}
