package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long>, JpaSpecificationExecutor<TransactionRecord> {
    List<TransactionRecord> findByReconciliationStatusAndTransactionTimeBetween(String status, LocalDateTime start, LocalDateTime end);
    List<TransactionRecord> findByUploadId(String uploadId);
    
    // 用于分类删除时校验关联
    boolean existsByCategoryOrSubCategory(String category, String subCategory);
}
