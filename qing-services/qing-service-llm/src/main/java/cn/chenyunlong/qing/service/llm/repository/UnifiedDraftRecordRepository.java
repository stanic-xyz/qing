package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnifiedDraftRecordRepository extends JpaRepository<UnifiedDraftRecord, Long> {

    Page<UnifiedDraftRecord> findByBatchId(Long batchId, Pageable pageable);

    Page<UnifiedDraftRecord> findByBatchIdAndMatchStatus(Long batchId, DraftMatchStatusEnum matchStatus, Pageable pageable);

    long countByBatchId(Long batchId);
}
