package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_record")
@Data
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime transactionTime;
    private String channel;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String accountName; // 冗余
    private String accountType;

    private String type; // INCOME/EXPENSE/TRANSFER
    private BigDecimal amount;
    private BigDecimal balance;
    private String counterparty;
    private String merchant;
    private String category;
    private String subCategory;
    private String status;
    private BigDecimal fee;
    private String originalId;
    private String sourceFile;
    private String remark;
    private String tags; // JSON

    private Long linkedId;
    private String linkedGroupId; // 用于比对的关联ID
    
    private String uploadId; // 关联的导入批次ID
    
    private Boolean isDeleted = false; // 软删除标记
    private Boolean isModified = false; // 修改标记
    
    @Column(columnDefinition = "TEXT")
    private String originalData; // 存储 JSON 格式的原始解析数据

    private String reconciliationStatus; // PENDING/MATCHED/MANUAL
    private Boolean confirmed;

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
