package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long>, JpaSpecificationExecutor<TransactionRecord> {
    List<TransactionRecord> findByReconciliationStatusAndTransactionTimeBetween(ReconciliationStatusEnum reconciliationStatus, LocalDateTime transactionTime, LocalDateTime transactionTime2);

    List<TransactionRecord> findByUploadId(String uploadId);

    Page<TransactionRecord> findByUploadId(String uploadId, Pageable pageable);

    // 用于跨账单撮合查找潜在匹配
    List<TransactionRecord> findByAmountAndTransactionTimeBetweenAndIsImportedTrue(BigDecimal amount, LocalDateTime start, LocalDateTime end);

    // 用于分类删除时校验关联
    boolean existsByCategory(Category category);

    long countByCategory(Category category);

    List<TransactionRecord> findAllByAccount(Account account);

    long countByAccount(Account account);

    List<TransactionRecord> findAllByAccountIdAndTransactionTimeBetween(Long accountId, LocalDateTime transactionTimeAfter, LocalDateTime transactionTimeBefore);

    boolean existsByOriginalId(String originalId);

    @Query("select t.originalId from TransactionRecord t where t.originalId in :originalIds")
    List<String> findExistingOriginalIds(@Param("originalIds") Collection<String> originalIds);
}
