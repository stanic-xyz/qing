package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DraftCommitService {

    private final UnifiedDraftBatchRepository batchRepository;
    private final UnifiedDraftRecordRepository recordRepository;
    private final TransactionRecordRepository transactionRepo;
    private final AccountRepository accountRepository;

    public record CommitResult(int importedCount, int duplicateCount, int skippedCount, String message) {}

    @Transactional
    public CommitResult commit(Long batchId) {
        UnifiedDraftBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new NoSuchElementException("draft batch not found"));

        if (batch.getStatus() == DraftBatchStatusEnum.IMPORTED) {
            throw new IllegalStateException("批次已入账，不得重复提交");
        }

        List<UnifiedDraftRecord> draftRecords = recordRepository.findByBatchId(batchId, Pageable.unpaged()).getContent();

        if (draftRecords.isEmpty()) {
            throw new IllegalStateException("草稿记录为空，无法入账");
        }

        Account account = inferAccount(draftRecords);

        int imported = 0;
        int duplicate = 0;
        int skipped = 0;

        Map<Long, Account> accountsToUpdate = new HashMap<>();
        List<TransactionRecord> toSave = new ArrayList<>();

        for (UnifiedDraftRecord dr : draftRecords) {
            if (dr.getMatchStatus() == DraftMatchStatusEnum.UNMATCHED) {
                skipped++;
                continue;
            }

            TransactionRecord tr = new TransactionRecord();
            tr.setAccount(account);
            tr.setChannel(account != null ? account.getChannel() : null);
            tr.setAccountName(account != null ? account.getAccountName() : null);
            tr.setAccountType(account != null ? account.getAccountType() : null);
            tr.setTransactionTime(dr.getTransactionTime());
            tr.setType(TrasactionType.valueOf(dr.getDirection() != null ? dr.getDirection() : "EXPENSE"));
            tr.setAmount(dr.getAmount());

            // 执行商户信息
            Counterparty finalCounterparty = dr.getFinalCounterparty();
            if (finalCounterparty != null) {
                tr.setCounterparty(finalCounterparty);
            }
            tr.setMerchant(dr.getMerchant());
            tr.setUploadId(String.valueOf(batchId));
            tr.setBatchNo(batch.getBatchNo());
            tr.setMatchStatus(dr.getMatchStatus() == DraftMatchStatusEnum.MATCHED
                    ? MatchStatusEnum.AUTO_MATCHED : MatchStatusEnum.ORIGINAL);
            tr.setIsImported(true);
            tr.setOriginalData(dr.getRawPayload());

            toSave.add(tr);
        }

        if (!toSave.isEmpty() && account != null) {
            Account accToUpdate = accountsToUpdate.computeIfAbsent(account.getId(), k -> account);
            for (TransactionRecord tr : toSave) {
                BigDecimal current = accToUpdate.getCurrentBalance() != null
                        ? accToUpdate.getCurrentBalance() : BigDecimal.ZERO;
                if (tr.getType() == TrasactionType.INCOME) {
                    accToUpdate.setCurrentBalance(current.add(tr.getAmount()));
                } else if (tr.getType() == TrasactionType.EXPENSE || tr.getType() == TrasactionType.TRANSFER) {
                    accToUpdate.setCurrentBalance(current.subtract(tr.getAmount()));
                }
            }
        }

        if (!accountsToUpdate.isEmpty()) {
            accountRepository.saveAll(accountsToUpdate.values());
        }

        List<TransactionRecord> saved = transactionRepo.saveAll(toSave);
        imported = saved.size();

        batch.setStatus(DraftBatchStatusEnum.IMPORTED);
        batch.setProgress(100);
        batchRepository.save(batch);

        log.info("commit完成: batchId={}, imported={}, duplicate={}, skipped={}", batchId, imported, duplicate, skipped);
        return new CommitResult(imported, duplicate, skipped,
                String.format("入账成功 %d 条，重复 %d 条，跳过 %d 条", imported, duplicate, skipped));
    }

    private Account inferAccount(List<UnifiedDraftRecord> records) {
        if (records == null || records.isEmpty()) {
            return null;
        }
        Long batchId = records.get(0).getBatchId();
        if (batchId == null) {
            return null;
        }
        UnifiedDraftBatch batch = batchRepository.findById(batchId).orElse(null);
        if (batch == null || batch.getAccountId() == null) {
            return null;
        }
        return accountRepository.findById(batch.getAccountId()).orElse(null);
    }
}
