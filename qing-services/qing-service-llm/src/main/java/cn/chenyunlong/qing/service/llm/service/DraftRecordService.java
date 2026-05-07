package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DraftRecordService {

    private final UnifiedDraftRecordRepository draftRecordRepository;

    @Transactional(readOnly = true)
    public Page<UnifiedDraftRecord> pageByBatchId(Long batchId, int page, int size) {
        return draftRecordRepository.findByBatchId(batchId, PageRequest.of(page, size));
    }
}
