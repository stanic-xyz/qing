package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnifiedDraftBatchRepository extends JpaRepository<UnifiedDraftBatch, Long> {

    Optional<UnifiedDraftBatch> findByBatchNo(String batchNo);
}
