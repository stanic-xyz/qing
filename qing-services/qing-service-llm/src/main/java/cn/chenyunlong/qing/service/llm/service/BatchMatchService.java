package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.*;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchMatchService {

    private final UnifiedDraftBatchRepository batchRepository;
    private final UnifiedDraftRecordRepository draftRecordRepository;
    private final TransactionRecordRepository transactionRepo;
    private final MatcherService matcherService;
    private final TransactionTemplate transactionTemplate;  // 编程式事务，更灵活

    /**
     * 匹配单个批次（独立事务）
     *
     * @param batchId                批次ID
     * @param account                所属账户（用于查询历史）
     * @param historyRecordsSnapshot 历史记录快照（不可变列表）
     * @param lockedTempIds          被锁定的临时记录ID列表
     * @param immutableRules         匹配规则
     */
    public void matchSingleBatch(Long batchId, Account account,
                                 List<TransactionRecord> historyRecordsSnapshot,
                                 List<Long> lockedTempIds,
                                 List<TransactionMatcher> immutableRules) {
        // 使用编程式事务确保每个批次独立提交/回滚
        transactionTemplate.execute(status -> {
            try {
                // 1. 重新查询批次（确保最新状态且处于当前事务）
                UnifiedDraftBatch batch = batchRepository.findById(batchId)
                        .orElseThrow(() -> new RuntimeException("批次不存在: " + batchId));

                // 状态校验：只有 DRAFTED 或 MATCHING（重试）状态才允许匹配
                if (batch.getStatus() != DraftBatchStatusEnum.DRAFTED
                        && batch.getStatus() != DraftBatchStatusEnum.MATCHING) {
                    log.warn("批次 {} 状态为 {}，跳过匹配", batchId, batch.getStatus());
                    return null;
                }

                // 更新状态为匹配中
                batch.setStatus(DraftBatchStatusEnum.MATCHING);
                batchRepository.save(batch);

                // 2. 查询该批次下的所有草稿记录（最新）
                List<UnifiedDraftRecord> draftRecords = draftRecordRepository.findAllByBatch(batch);
                if (draftRecords.isEmpty()) {
                    batch.setStatus(DraftBatchStatusEnum.MATCHED);
                    batchRepository.save(batch);
                    return null;
                }

                // 3. 应用匹配器
                for (UnifiedDraftRecord record : draftRecords) {
                    if (lockedTempIds != null && lockedTempIds.contains(record.getId())) {
                        continue; // 跳过锁定的记录
                    }

                    record.setMatchStatus(DraftMatchStatusEnum.ORIGINAL);
                    record.setMatchRules(new ArrayList<>());
                    record.setIsModified(false);

                    // 传入历史记录快照，避免重复查询数据库,假设已经匹配完成了
                    // matcherService.applyMatchers(record, immutableRules);

                    record.setMatchStatus(DraftMatchStatusEnum.MATCHED);
                }

                draftRecordRepository.saveAll(draftRecords);  // 更新匹配状态

                //                // 4. 转换为正式交易记录（如果需要）
                //                List<TransactionRecord> transactionRecords = draftRecords.stream()
                //                        .map(DraftCommitService::convert)
                //                        .collect(Collectors.toList());
                //
                //                // 5. 批量保存（使用 JPA batch）
                //                transactionRepo.saveAll(transactionRecords);

                // 6. 更新批次完成状态
                batch.setStatus(DraftBatchStatusEnum.MATCHED);
                batch.setMatchedRecords((int) draftRecords.stream()
                        .filter(r -> r.getMatchStatus() == DraftMatchStatusEnum.MATCHED).count());
                batchRepository.save(batch);
                log.info("批次 {} 匹配完成，共 {} 条记录", batchId, draftRecords.size());

            } catch (Exception e) {
                log.error("批次 {} 匹配失败", batchId, e);
                // 标记批次失败，方便重试
                transactionTemplate.execute(status2 -> {
                    batchRepository.findById(batchId).ifPresent(b -> {
                        b.setStatus(DraftBatchStatusEnum.FAILED);
                        batchRepository.save(b);
                    });
                    return null;
                });
                throw new RuntimeException("批次匹配失败", e);
            }
            return null;
        });
    }
}
