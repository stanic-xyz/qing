package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.ReconciliationRecord;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.ReconciliationRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReconciliationService {

    private final TransactionRecordRepository transactionRepo;

    private final ReconciliationRecordRepository reconciliationRepo;

    // 自动对账（可按需调用）
    public void autoReconcile(LocalDateTime start, LocalDateTime end) {
        List<TransactionRecord> pendingRecords = transactionRepo.findByReconciliationStatusAndTransactionTimeBetween(ReconciliationStatusEnum.PENDING, start, end);

        // 简单规则匹配：按时间窗口分组，然后比较金额
        Map<String, List<TransactionRecord>> groups = pendingRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getTransactionTime().toLocalDate().toString()));

        for (List<TransactionRecord> dayRecords : groups.values()) {
            matchWithinDay(dayRecords);
        }
    }

    private void matchWithinDay(List<TransactionRecord> records) {
        // 双重循环比较（后续可用向量匹配优化）
        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                TransactionRecord record = records.get(i);
                TransactionRecord b = records.get(j);
                if (isMatching(record, b)) {
                    // 标记匹配
                    record.setLinkedId(b.getId()); // 或双向关联
                    record.setReconciliationStatus(ReconciliationStatusEnum.MATCHED);
                    b.setLinkedId(record.getId());
                    b.setReconciliationStatus(ReconciliationStatusEnum.MATCHED);
                    transactionRepo.saveAll(Arrays.asList(record, b));

                    // 记录对账记录
                    ReconciliationRecord reconciliationRecord = new ReconciliationRecord();
                    reconciliationRecord.setTransactionIds(record.getId() + "," + b.getId());
                    reconciliationRecord.setReconciliationType("DUPLICATE");
                    reconciliationRecord.setStatus("RESOLVED");
                    reconciliationRecord.setResolvedBy("AUTO");
                    reconciliationRepo.save(reconciliationRecord);
                }
            }
        }
    }

    private boolean isMatching(TransactionRecord a, TransactionRecord b) {
        // 简单匹配规则：时间差<5分钟，金额相同，渠道不同
        if (!Objects.equals(a.getAccount().getId(), b.getAccount().getId())) {
            if (Math.abs(a.getAmount().compareTo(b.getAmount())) == 0) {
                long minutes = Math.abs(ChronoUnit.MINUTES.between(a.getTransactionTime(), b.getTransactionTime()));
                return minutes <= 5;
            }
        }
        return false;
    }

    public void autoReconcileForRecords(List<TransactionRecord> records) {
        // Simple mock implementation for demo
        matchWithinDay(records);
    }

    // 手动匹配
    @Transactional
    public void manualMatch(List<Long> transactionIds, Long masterId, String notes) {
        List<TransactionRecord> records = transactionRepo.findAllById(transactionIds);
        for (TransactionRecord r : records) {
            if (!r.getId().equals(masterId)) {
                r.setLinkedId(masterId);
                r.setReconciliationStatus(ReconciliationStatusEnum.MANUAL);
            }
        }
        transactionRepo.saveAll(records);

        ReconciliationRecord rec = new ReconciliationRecord();
        rec.setTransactionIds(transactionIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        rec.setReconciliationType("DUPLICATE");
        rec.setStatus("RESOLVED");
        rec.setResolvedBy("MANUAL");
        rec.setNotes(notes);
        reconciliationRepo.save(rec);
    }
}
