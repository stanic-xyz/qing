package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.*;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

        Account account = batch.getAccount();

        List<UnifiedDraftRecord> draftRecords = recordRepository.findByBatchId(batchId, Pageable.unpaged()).getContent();

        if (draftRecords.isEmpty()) {
            throw new IllegalStateException("草稿记录为空，无法入账");
        }

        int imported = 0;
        AtomicInteger duplicate = new AtomicInteger(0);
        int skipped = 0;

        Map<Long, Account> accountsToUpdate = new HashMap<>();
        List<TransactionRecord> newTransactionRecords = new ArrayList<>();

        for (UnifiedDraftRecord dr : draftRecords) {
            if (dr.getMatchStatus() == DraftMatchStatusEnum.UNMATCHED) {
                skipped++;
                continue;
            }
            TransactionRecord tr = convert(dr, batch);
            newTransactionRecords.add(tr);
        }

        List<TransactionRecord> transactionRecordList = transactionRepo.findAllByAccount(account);
        log.info("入账前检查重复记录: accountId={}, existingCount={}, toImportCount={}",
                account != null ? account.getId() : null, transactionRecordList.size(), newTransactionRecords.size());


        // 1. 构建已有记录的复合键集合（用于快速查重）
        Set<String> existingKeys = transactionRecordList.stream()
                .map(tr -> buildDuplicateKey(tr.getTransactionTime(), tr.getAmount(), tr.getMerchant()))
                .collect(Collectors.toSet());

        List<TransactionRecord> recordList = newTransactionRecords
                .stream().filter(tr -> {
                    String key = buildDuplicateKey(tr.getTransactionTime(), tr.getAmount(), tr.getMerchant());
                    if (existingKeys.contains(key)) {
                        duplicate.incrementAndGet();
                        log.warn("检测到重复记录，跳过入账: accountId={}, transactionTime={}, amount={}, merchant={}",
                                tr.getAccount() != null ? tr.getAccount().getId() : null,
                                tr.getTransactionTime(), tr.getAmount(), tr.getMerchant());
                        return false; // 重复，过滤掉
                    }
                    return true; // 不重复，保留
                }).toList();


        if (!recordList.isEmpty() && account != null) {
            Account accToUpdate = accountsToUpdate.computeIfAbsent(account.getId(), k -> account);
            for (TransactionRecord transactionRecord : recordList) {
                BigDecimal current = accToUpdate.getCurrentBalance() != null
                        ? accToUpdate.getCurrentBalance() : BigDecimal.ZERO;
                accToUpdate.setCurrentBalance(current.add(transactionRecord.getAmount()));
            }
        }

        if (!accountsToUpdate.isEmpty()) {
            accountRepository.saveAll(accountsToUpdate.values());
        }


        List<TransactionRecord> saved = transactionRepo.saveAll(recordList);
        imported = saved.size();

        batch.setStatus(DraftBatchStatusEnum.IMPORTED);
        batch.setProgress(100);
        batchRepository.save(batch);

        log.info("commit完成: batchId={}, imported={}, duplicate={}, skipped={}", batchId, imported, duplicate, skipped);
        return new CommitResult(imported, duplicate.get(), skipped,
                String.format("入账成功 %d 条，重复 %d 条，跳过 %d 条", imported, duplicate.get(), skipped));
    }

    private String buildDuplicateKey(LocalDateTime transactionTime, BigDecimal amount, String merchant) {
        // 注意：时间可能只精确到秒，可以根据业务调整精度
        // 1. 只取日期部分（yyyy-MM-dd）
        String date = transactionTime.toLocalDate().toString();
        // 2. 金额统一去除末尾零，避免 1.0 和 1.00 被认为不同
        String amountStr = amount.stripTrailingZeros().toString();
        // 3. 商户为空时用空字符串替代
        String merchantStr = merchant == null ? "" : merchant;
        return date + "|" + amountStr + "|" + merchantStr;
    }

    public static @NonNull TransactionRecord convert(UnifiedDraftRecord dr, UnifiedDraftBatch batch) {

        Account account = batch.getAccount();

        TransactionRecord tr = new TransactionRecord();
        tr.setAccount(account);
        tr.setChannel(account != null ? account.getChannel() : null);
        tr.setAccountName(account != null ? account.getAccountName() : null);
        tr.setAccountType(account != null ? account.getAccountType() : null);
        tr.setTransactionTime(dr.getTransactionTime());
        tr.setType(dr.getTrasactionType());
        tr.setAmount(dr.getAmount());

        // 执行商户信息
        Counterparty finalCounterparty = dr.getFinalCounterparty();
        if (finalCounterparty != null) {
            tr.setCounterparty(finalCounterparty);
        }

        tr.setMerchant(dr.getMerchant());
        tr.setUploadId(String.valueOf(batch.getId()));
        tr.setBatchNo(batch.getBatchNo());
        switch (dr.getMatchStatus()) {
            case MATCHED -> tr.setMatchStatus(MatchStatusEnum.AUTO_MATCHED);
            case REVIEW_REQUIRED -> tr.setMatchStatus(MatchStatusEnum.SUSPICIOUS);
            case null, default -> tr.setMatchStatus(MatchStatusEnum.ORIGINAL);
        }
        tr.setIsImported(true);
        tr.setOriginalData(dr.getRawPayload());
        return tr;
    }
}
