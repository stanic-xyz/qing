package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

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

    private Integer progress = 0;

    private Integer totalRecords = 0;

    private Long accountId;

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
