package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.hutool.core.net.multipart.UploadFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_unified_draft_batch")
@Data
public class UnifiedDraftBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String batchNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdapterTypeEnum adapterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DraftBatchStatusEnum status = DraftBatchStatusEnum.DRAFTED;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private UploadFileRecord uploadFile;

    private Integer progress = 0;

    private Integer totalRecords = 0;
    private Integer matchedRecords = 0;    // 已匹配数
    private Integer unmatchedRecords = 0;   // 未匹配数
    private Integer suspiciousRecords = 0;  // 存疑数

    private BigDecimal totalIncome = BigDecimal.ZERO;   // 总收入
    private BigDecimal totalExpense = BigDecimal.ZERO;  // 总支出

    private LocalDateTime transactionStartTime; // 最早交易时间
    private LocalDateTime transactionEndTime;   // 最晚交易时间

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

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
