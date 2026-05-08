package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnifiedDraftRecordRepository extends JpaRepository<UnifiedDraftRecord, Long> {

    Page<UnifiedDraftRecord> findByFileRecord(UploadFileRecord fileRecord, Pageable pageable);

    Page<UnifiedDraftRecord> findByBatchId(Long batchId, Pageable pageable);

    Page<UnifiedDraftRecord> findByBatchIdAndMatchStatus(Long batchId, DraftMatchStatusEnum matchStatus, Pageable pageable);

    long countByBatchId(Long batchId);

    List<UnifiedDraftRecord> findAllByBatch(UnifiedDraftBatch batch);

    List<UnifiedDraftRecord> findAllByFileRecord(UploadFileRecord fileRecord);
}
