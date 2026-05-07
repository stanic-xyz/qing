package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DraftRecordService {

    private final UnifiedDraftRecordRepository draftRecordRepository;

    @Transactional(readOnly = true)
    public Page<UnifiedDraftRecord> pageByBatchId(Long batchId, int page, int size, String matchStatus) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime", "id"));
        if (StringUtils.hasText(matchStatus)) {
            return draftRecordRepository.findByBatchIdAndMatchStatus(batchId, matchStatus, pageRequest);
        }
        return draftRecordRepository.findByBatchId(batchId, pageRequest);
    }
}
