package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_unified_draft_record")
@Data
public class UnifiedDraftRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long batchId;

    private String sourceRecordId;

    private LocalDateTime transactionTime;

    private String direction;

    private BigDecimal amount;

    private String counterparty;

    private String merchant;

    @Enumerated(EnumType.STRING)
    private DraftMatchStatusEnum matchStatus;

    @Column(columnDefinition = "TEXT")
    private String rawPayload;

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
