package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.BatchStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_upload_batch")
@Data
public class UploadBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String uploadId;           // 关联的上传记录ID

    private String batchNo;            // 批次号：uploadId_batchIndex

    @Enumerated(EnumType.STRING)
    private BatchStatusEnum status = BatchStatusEnum.PENDING;  // 批次状态

    private Integer totalRecords = 0;      // 总记录数
    private Integer matchedRecords = 0;    // 已匹配数
    private Integer unmatchedRecords = 0;   // 未匹配数
    private Integer suspiciousRecords = 0;  // 存疑数

    private BigDecimal totalIncome = BigDecimal.ZERO;   // 总收入
    private BigDecimal totalExpense = BigDecimal.ZERO;  // 总支出

    private LocalDateTime transactionStartTime; // 最早交易时间
    private LocalDateTime transactionEndTime;   // 最晚交易时间

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
