package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long>, JpaSpecificationExecutor<TransactionRecord> {
    List<TransactionRecord> findByReconciliationStatusAndTransactionTimeBetween(ReconciliationStatusEnum reconciliationStatus, LocalDateTime transactionTime, LocalDateTime transactionTime2);

    List<TransactionRecord> findByUploadId(String uploadId);

    // 用于跨账单撮合查找潜在匹配
    List<TransactionRecord> findByAmountAndTransactionTimeBetweenAndIsImportedTrue(BigDecimal amount, LocalDateTime start, LocalDateTime end);

    // 用于分类删除时校验关联
    boolean existsByCategory(Category category);

    List<TransactionRecord> findAllByAccount(Account account);
}
