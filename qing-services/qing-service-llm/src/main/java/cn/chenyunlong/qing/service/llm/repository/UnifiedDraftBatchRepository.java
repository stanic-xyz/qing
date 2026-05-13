package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnifiedDraftBatchRepository extends JpaRepository<UnifiedDraftBatch, Long> {

    Optional<UnifiedDraftBatch> findByBatchNo(String batchNo);

    Page<UnifiedDraftBatch> findByUploadFile(UploadFileRecord uploadFile, Pageable pageable);

    // 悲观锁查询
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from UnifiedDraftBatch b where b.id = :id")
    Optional<UnifiedDraftBatch> findByIdWithLock(@Param("id") Long id);

    List<UnifiedDraftBatch> findAllByUploadFile(UploadFileRecord uploadFile);
}
