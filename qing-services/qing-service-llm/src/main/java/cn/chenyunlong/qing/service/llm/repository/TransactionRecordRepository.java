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

    List<TransactionRecord> findByAmountAndTransactionTimeBetweenAndIsImportedTrue(BigDecimal amount, LocalDateTime start, LocalDateTime end);

    boolean existsByCategory(Category category);

    long countByCategory(Category category);

    List<TransactionRecord> findAllByAccount(Account account);

    long countByAccount(Account account);

    List<TransactionRecord> findAllByAccountIdAndTransactionTimeBetween(Long accountId, LocalDateTime transactionTimeAfter, LocalDateTime transactionTimeBefore);

    boolean existsByOriginalId(String originalId);

    @Query("select t.originalId from TransactionRecord t where t.originalId in :originalIds")
    List<String> findExistingOriginalIds(@Param("originalIds") Collection<String> originalIds);

    /**
     * 获取指定时间范围内的交易记录（带关联实体预加载）
     * 用于 Dashboard 统计查询，避免 N+1 问题
     *
     * @param startTime 开始时间（包含）
     * @param endTime   结束时间（不包含）
     * @return 时间范围内的交易记录列表
     */
    @Query("SELECT t FROM TransactionRecord t " +
           "LEFT JOIN FETCH t.account " +
           "LEFT JOIN FETCH t.category " +
           "WHERE t.transactionTime >= :startTime " +
           "AND t.transactionTime < :endTime " +
           "AND (t.isImported IS NULL OR t.isImported = true) " +
           "AND (t.isDeleted IS NULL OR t.isDeleted = false)")
    List<TransactionRecord> findAllWithDetailsInTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 数据库层面月度聚合统计（按交易类型）
     * 直接在数据库层面完成聚合，减少数据传输
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 包含 [transactionType, count, totalAmount] 的数组列表
     */
    @Query("SELECT t.transactionType, COUNT(t), COALESCE(SUM(t.amount), 0) " +
           "FROM TransactionRecord t " +
           "WHERE t.transactionTime >= :startTime " +
           "AND t.transactionTime < :endTime " +
           "AND (t.isImported IS NULL OR t.isImported = true) " +
           "AND (t.isDeleted IS NULL OR t.isDeleted = false) " +
           "GROUP BY t.transactionType")
    List<Object[]> getMonthlyStatsAggregation(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 数据库层面月度分类支出统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 包含 [categoryName, totalAmount] 的数组列表
     */
    @Query("SELECT c.name, COALESCE(SUM(t.amount), 0) " +
           "FROM TransactionRecord t " +
           "LEFT JOIN t.category c " +
           "WHERE t.transactionTime >= :startTime " +
           "AND t.transactionTime < :endTime " +
           "AND t.transactionType = 'EXPENSE' " +
           "AND (t.isImported IS NULL OR t.isImported = true) " +
           "AND (t.isDeleted IS NULL OR t.isDeleted = false) " +
           "GROUP BY c.name " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> getCategorySpendingAggregation(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 数据库层面每日收支趋势统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 包含 [date, transactionType, totalAmount] 的数组列表
     */
    @Query(value = "SELECT CAST(t.transaction_time AS DATE), t.transaction_type, COALESCE(SUM(t.amount), 0) " +
                   "FROM finance_transaction_record t " +
                   "WHERE t.transaction_time >= :startTime " +
                   "AND t.transaction_time < :endTime " +
                   "AND (t.is_imported IS NULL OR t.is_imported = true) " +
                   "AND (t.is_deleted IS NULL OR t.is_deleted = false) " +
                   "GROUP BY CAST(t.transaction_time AS DATE), t.transaction_type " +
                   "ORDER BY CAST(t.transaction_time AS DATE)",
           nativeQuery = true)
    List<Object[]> getDailyTrendsAggregation(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
