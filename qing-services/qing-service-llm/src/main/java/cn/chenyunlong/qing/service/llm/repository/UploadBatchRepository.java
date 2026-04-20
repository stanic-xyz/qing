package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UploadBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadBatchRepository extends JpaRepository<UploadBatch, Long> {

    List<UploadBatch> findByUploadIdOrderByBatchNoAsc(String uploadId);

    Optional<UploadBatch> findByBatchNo(String batchNo);

    long countByUploadId(String uploadId);
}
