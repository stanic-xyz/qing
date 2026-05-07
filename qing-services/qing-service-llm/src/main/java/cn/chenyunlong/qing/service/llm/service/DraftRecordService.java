package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DraftRecordService {

    private final UnifiedDraftRecordRepository draftRecordRepository;

    @Transactional(readOnly = true)
    public Page<UnifiedDraftRecord> pageByBatchId(Long batchId, int page, int size, DraftMatchStatusEnum matchStatus) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime", "id"));
        if (matchStatus != null) {
            return draftRecordRepository.findByBatchIdAndMatchStatus(batchId, matchStatus, pageRequest);
        }
        return draftRecordRepository.findByBatchId(batchId, pageRequest);
    }

    @Transactional
    public UnifiedDraftRecord lockFields(Long recordId, List<String> fields) {
        UnifiedDraftRecord record = draftRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("draft record not found: " + recordId));
        String current = record.getLockedFields();
        String newLocked = fields == null || fields.isEmpty() ? null : String.join(",", fields);
        record.setLockedFields(newLocked);
        log.info("锁定字段: recordId={}, fields={}, previous={}", recordId, newLocked, current);
        return draftRecordRepository.save(record);
    }

    @Transactional
    public UnifiedDraftRecord unlockFields(Long recordId) {
        UnifiedDraftRecord record = draftRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("draft record not found: " + recordId));
        String previous = record.getLockedFields();
        record.setLockedFields(null);
        log.info("解锁字段: recordId={}, previous={}", recordId, previous);
        return draftRecordRepository.save(record);
    }
}
