package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
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

    private final UnifiedDraftBatchRepository batchRepository;
    private final UnifiedDraftRecordRepository draftRecordRepository;

    /**
     * 按批次分页查询草稿记录，批次不存在时抛出资源不存在异常。
     *
     * @param batchId 批次 ID
     * @param page 页码
     * @param size 页大小
     * @param matchStatus 匹配状态
     * @return 草稿记录分页结果
     */
    @Transactional(readOnly = true)
    public Page<UnifiedDraftRecord> pageByBatchId(Long batchId, int page, int size, DraftMatchStatusEnum matchStatus) {
        getBatchOrThrow(batchId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime", "id"));
        if (matchStatus != null) {
            return draftRecordRepository.findByBatchIdAndMatchStatus(batchId, matchStatus, pageRequest);
        }
        return draftRecordRepository.findByBatchId(batchId, pageRequest);
    }

    @Transactional
    public UnifiedDraftRecord lockFields(Long recordId, List<String> fields) {
        UnifiedDraftRecord record = getDraftRecordOrThrow(recordId);
        String current = record.getLockedFields();
        String newLocked = fields == null || fields.isEmpty() ? null : String.join(",", fields);
        record.setLockedFields(newLocked);
        log.info("锁定字段: recordId={}, fields={}, previous={}", recordId, newLocked, current);
        return draftRecordRepository.save(record);
    }

    @Transactional
    public UnifiedDraftRecord unlockFields(Long recordId) {
        UnifiedDraftRecord record = getDraftRecordOrThrow(recordId);
        String previous = record.getLockedFields();
        record.setLockedFields(null);
        log.info("解锁字段: recordId={}, previous={}", recordId, previous);
        return draftRecordRepository.save(record);
    }

    /**
     * 按 ID 加载草稿批次，不存在时抛出资源不存在异常。
     *
     * @param batchId 批次 ID
     * @return 草稿批次实体
     */
    private UnifiedDraftBatch getBatchOrThrow(Long batchId) {
        return batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("草稿批次不存在: " + batchId));
    }

    /**
     * 按 ID 加载草稿记录，不存在时抛出资源不存在异常。
     *
     * @param recordId 草稿记录 ID
     * @return 草稿记录实体
     */
    private UnifiedDraftRecord getDraftRecordOrThrow(Long recordId) {
        return draftRecordRepository.findById(recordId)
                .orElseThrow(() -> new NotFoundException("草稿记录不存在: " + recordId));
    }
}
