package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.CategoryRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("BatchMatchService Integration Tests")
class BatchMatchServiceTest {

    @Autowired
    private BatchMatchService batchMatchService;

    @Autowired
    private MatcherService matcherService;

    @Autowired
    private UnifiedDraftRecordRepository draftRecordRepository;

    @Autowired
    private UnifiedDraftBatchRepository batchRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private UnifiedDraftBatch testBatch;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Create test account
        testAccount = new Account();
        testAccount.setAccountName("Test Account");
        testAccount.setAccountType(AccountType.DEBIT);
        testAccount.setStatus(AccountStatusEnum.ACTIVE);
        testAccount = accountRepository.save(testAccount);

        // Create test batch with account associated
        testBatch = new UnifiedDraftBatch();
        testBatch.setBatchNo("test-batch-" + System.currentTimeMillis());
        testBatch.setStatus(DraftBatchStatusEnum.MATCHING);
        testBatch.setAccount(testAccount);
        testBatch.setAdapterType(AdapterTypeEnum.PARSER);
        testBatch = batchRepository.save(testBatch);
    }

    @Test
    @DisplayName("matchSingleBatch - should apply matching rules to all records")
    void testMatchSingleBatch_Success() {
        // Given: a batch with draft records
        UnifiedDraftRecord record = createTestRecord("online shopping", new BigDecimal("100.00"));
        record.setMatchStatus(DraftMatchStatusEnum.ORIGINAL);
        draftRecordRepository.save(record);

        // When: matching is executed
        batchMatchService.matchSingleBatch(
            testBatch.getId(),
            testAccount,
            new ArrayList<>(),
            null,
            new ArrayList<>()
        );

        // Then: record status is updated to UNMATCHED (no rules matched)
        UnifiedDraftRecord matched = draftRecordRepository.findById(record.getId()).orElseThrow();
        assertThat(matched.getMatchStatus()).isNotEqualTo(DraftMatchStatusEnum.ORIGINAL);
    }

    @Test
    @DisplayName("matchSingleBatch - should skip locked records")
    void testMatchSingleBatch_SkipsLockedRecords() {
        // Given: a locked record
        UnifiedDraftRecord record = createTestRecord("test", new BigDecimal("50.00"));
        record.setMatchStatus(DraftMatchStatusEnum.ORIGINAL);
        record = draftRecordRepository.save(record);
        List<Long> lockedIds = List.of(record.getId());

        // When: matching is executed with locked IDs
        batchMatchService.matchSingleBatch(
            testBatch.getId(),
            testAccount,
            new ArrayList<>(),
            lockedIds,
            new ArrayList<>()
        );

        // Then: locked record is unchanged
        UnifiedDraftRecord unchanged = draftRecordRepository.findById(record.getId()).orElseThrow();
        assertThat(unchanged.getMatchStatus()).isEqualTo(DraftMatchStatusEnum.ORIGINAL);
    }

    @Test
    @DisplayName("matchSingleBatch - should handle empty batch")
    void testMatchSingleBatch_EmptyBatch() {
        // When: matching on empty batch
        // Then: no exception thrown
        batchMatchService.matchSingleBatch(
            testBatch.getId(),
            testAccount,
            new ArrayList<>(),
            null,
            new ArrayList<>()
        );
        // Passes if no exception
    }

    private UnifiedDraftRecord createTestRecord(String detail, BigDecimal amount) {
        UnifiedDraftRecord record = new UnifiedDraftRecord();
        record.setBatch(testBatch);
        record.setDetail(detail);
        record.setAmount(amount);
        record.setTransactionTime(LocalDateTime.now());
        record.setDirection(TransactionDirectionTypeEnum.EXPENSE);
        return record;
    }
}