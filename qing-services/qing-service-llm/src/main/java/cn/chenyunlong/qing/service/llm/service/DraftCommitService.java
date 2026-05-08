package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.FileUploadStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DraftCommitService {

    private final UnifiedDraftBatchRepository batchRepository;
    private final UnifiedDraftRecordRepository recordRepository;
    private final TransactionRecordRepository transactionRepo;
    private final AccountRepository accountRepository;
    private final UploadFileRecordRepository uploadFileRecordRepository;

    public record CommitResult(int importedCount, int duplicateCount, int skippedCount, String message) {}

    /**
     * 提交整个上传文件下的所有批次（入账）
     *
     * @param uploadId 上传文件记录ID
     * @return 汇总的提交结果
     */
    @Transactional
    public CommitResult commit(Long uploadId) {

        UploadFileRecord uploadFileRecord = uploadFileRecordRepository.findById(uploadId)
                .orElseThrow(() -> new NoSuchElementException("上传文件记录不存在: " + uploadId));

        if (uploadFileRecord.getStatus() == FileUploadStatusEnum.IMPORTED) {
            throw new IllegalStateException("该文件已全部入账，不得重复提交");
        }
        // 可选：检查是否有批次可提交，若无则直接返回

        List<UnifiedDraftBatch> batches = batchRepository.findAllByUploadFile(uploadFileRecord);
        if (batches.isEmpty()) {
            throw new IllegalStateException("该文件下没有草稿批次，无法入账");
        }

        int totalImported = 0;
        int totalDuplicate = 0;
        int totalSkipped = 0;
        List<String> errorBatchNos = new ArrayList<>();

        for (UnifiedDraftBatch batch : batches) {
            try {
                CommitResult batchResult = commitBatch(batch.getId());
                totalImported += batchResult.importedCount();
                totalDuplicate += batchResult.duplicateCount();
                totalSkipped += batchResult.skippedCount();
            } catch (Exception e) {
                log.error("批次 {} 提交失败: {}", batch.getBatchNo(), e.getMessage(), e);
                errorBatchNos.add(batch.getBatchNo());
                // 继续处理下一个批次，不中断整个文件
            }
        }

        // 全部批次处理完成后，更新文件整体状态
        if (errorBatchNos.isEmpty()) {
            uploadFileRecord.setStatus(FileUploadStatusEnum.IMPORTED);
            uploadFileRecordRepository.save(uploadFileRecord);
        } else {
            // 部分失败，可根据需要设置状态为 PARTIAL_IMPORTED 等
            uploadFileRecord.setStatus(FileUploadStatusEnum.PARTIAL_IMPORTED);
            uploadFileRecordRepository.save(uploadFileRecord);
            log.warn("文件 {} 部分批次提交失败: {}", uploadId, errorBatchNos);
        }

        String message = String.format("总入账成功 %d 条，重复 %d 条，跳过 %d 条，失败批次 %s",
                totalImported, totalDuplicate, totalSkipped, errorBatchNos);
        return new CommitResult(totalImported, totalDuplicate, totalSkipped, message);
    }

    /**
     * 提交单个批次（入账）—— 供外部单独调用
     *
     * @param batchId 批次ID
     * @return 该批次的提交结果
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)  // 独立事务，失败不影响其他批次
    public CommitResult commitBatch(Long batchId) {
        // 1. 查询批次（悲观锁防止并发）
        UnifiedDraftBatch batch = batchRepository.findByIdWithLock(batchId)
                .orElseThrow(() -> new NoSuchElementException("草稿批次不存在: " + batchId));

        // 2. 状态校验
        if (batch.getStatus() == DraftBatchStatusEnum.IMPORTED) {
            throw new IllegalStateException("批次已入账，不得重复提交");
        }
        if (batch.getStatus() != DraftBatchStatusEnum.MATCHED) {
            throw new IllegalStateException("批次当前状态为 " + batch.getStatus() + "，无法入账，请先完成匹配");
        }

        // 3. 获取账户并加锁
        Account account = batch.getAccount();
        if (account == null) {
            throw new IllegalStateException("批次未关联账户");
        }
        Account lockedAccount = accountRepository.findByIdWithLock(account.getId())
                .orElseThrow(() -> new NoSuchElementException("账户不存在: " + account.getId()));

        // 4. 获取该批次下的所有草稿记录
        List<UnifiedDraftRecord> draftRecords = recordRepository.findAllByBatch(batch);
        if (draftRecords.isEmpty()) {
            throw new IllegalStateException("批次草稿记录为空，无法入账");
        }

        // 5. 转换并分类
        int skipped = 0;
        List<TransactionRecord> toImport = new ArrayList<>();
        for (UnifiedDraftRecord dr : draftRecords) {
            if (dr.getMatchStatus() != DraftMatchStatusEnum.MATCHED) {
                skipped++;
                continue;
            }
            toImport.add(convert(dr));  // 使用已有的 convert 方法
        }

        if (toImport.isEmpty()) {
            // 没有可入账的记录，直接更新批次状态为已入账
            batch.setStatus(DraftBatchStatusEnum.IMPORTED);
            batch.setProgress(100);
            batchRepository.save(batch);
            return new CommitResult(0, 0, skipped, "该批次无可入账记录，已标记为已入账");
        }

        // 6. 重复检测
        Set<String> existingKeys = findExistingKeysForAccount(lockedAccount.getId(), toImport);
        List<TransactionRecord> recordsToSave = new ArrayList<>();
        int duplicateCount = 0;
        for (TransactionRecord tr : toImport) {
            String key = buildDuplicateKey(tr.getTransactionTime(), tr.getAmount(), tr.getMerchant());
            if (existingKeys.contains(key)) {
                duplicateCount++;
                log.warn("检测到重复记录，跳过入账: accountId={}, time={}, amount={}, merchant={}",
                        lockedAccount.getId(), tr.getTransactionTime(), tr.getAmount(), tr.getMerchant());
            } else {
                recordsToSave.add(tr);
            }
        }

        // 7. 保存交易记录并更新账户余额
        int importedCount = 0;
        if (!recordsToSave.isEmpty()) {
            BigDecimal totalDelta = recordsToSave.stream()
                    .map(TransactionRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            lockedAccount.setCurrentBalance(lockedAccount.getCurrentBalance().add(totalDelta));
            accountRepository.save(lockedAccount);

            transactionRepo.saveAll(recordsToSave);
            importedCount = recordsToSave.size();
        }

        // 8. 更新批次状态
        batch.setStatus(DraftBatchStatusEnum.IMPORTED);
        batch.setProgress(100);
        batchRepository.save(batch);

        log.info("批次入账完成: batchId={}, imported={}, duplicate={}, skipped={}",
                batchId, importedCount, duplicateCount, skipped);
        return new CommitResult(importedCount, duplicateCount, skipped,
                String.format("入账成功 %d 条，重复 %d 条，跳过 %d 条", importedCount, duplicateCount, skipped));
    }

    // ---------- 以下是辅助方法和转换逻辑 ----------

    private String buildDuplicateKey(LocalDateTime transactionTime, BigDecimal amount, String merchant) {
        String date = transactionTime.toLocalDate().toString();
        String amountStr = amount.stripTrailingZeros().toString();
        String merchantStr = merchant == null ? "" : merchant;
        return date + "|" + amountStr + "|" + merchantStr;
    }

    private Set<String> findExistingKeysForAccount(Long accountId, List<TransactionRecord> toImport) {
        if (toImport.isEmpty()) {
            return Set.of();
        }
        LocalDateTime minTime = toImport.stream()
                .map(TransactionRecord::getTransactionTime)
                .min(LocalDateTime::compareTo).orElse(null);
        LocalDateTime maxTime = toImport.stream()
                .map(TransactionRecord::getTransactionTime)
                .max(LocalDateTime::compareTo).orElse(null);

        List<TransactionRecord> existing = transactionRepo.findAllByAccountIdAndTransactionTimeBetween(accountId, minTime, maxTime);

        return existing.stream()
                .map(tr -> buildDuplicateKey(tr.getTransactionTime(), tr.getAmount(), tr.getMerchant()))
                .collect(Collectors.toSet());
    }

    // 已有的 convert 方法保持不变（稍作修改，避免 batch.getBatchNo() 空指针）
    public static @NonNull TransactionRecord convert(UnifiedDraftRecord dr) {
        UnifiedDraftBatch batch = dr.getBatch();
        // 注意：batch 可能为空？根据业务应该不为空
        if (batch == null) {
            throw new IllegalStateException("草稿记录未关联批次");
        }
        Account account = batch.getAccount();

        TransactionRecord tr = new TransactionRecord();
        tr.setAccount(account);
        tr.setChannel(account != null ? account.getChannel() : null);
        tr.setAccountName(account != null ? account.getAccountName() : null);
        tr.setAccountType(account != null ? account.getAccountType() : null);
        tr.setTransactionTime(dr.getTransactionTime());
        tr.setDirectionType(dr.getDirection());
        tr.setAmount(dr.getAmount());

        Counterparty finalCounterparty = dr.getFinalCounterparty();
        if (finalCounterparty != null) {
            tr.setCounterparty(finalCounterparty);
        }

        tr.setMerchant(dr.getMerchant());
        tr.setUploadId(String.valueOf(batch.getId()));  // 注意：这里原来用的 batch.getId()，但 batch 可能没有 uploadId 概念，可改为 batch.getUploadFileRecord().getId()
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
