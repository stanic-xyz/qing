package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    private TransactionDirectionTypeEnum direction;

    private TrasactionType trasactionType;

    private BigDecimal amount;

    private String counterparty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Counterparty finalCounterparty;

    private String merchant;

    @Enumerated(EnumType.STRING)
    private DraftMatchStatusEnum matchStatus;

    @Column(columnDefinition = "TEXT")
    private String rawPayload;

    @Column(columnDefinition = "TEXT")
    private String lockedFields;

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
