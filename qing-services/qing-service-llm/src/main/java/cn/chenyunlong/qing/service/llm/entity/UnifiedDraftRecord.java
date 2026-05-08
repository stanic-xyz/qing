package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.TrasactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "finance_unified_draft_record")
@Data
public class UnifiedDraftRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private UploadFileRecord fileRecord;

    @JsonIgnore
    @ManyToOne
    private UnifiedDraftBatch batch;

    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    private TransactionDirectionTypeEnum direction;

    @Enumerated(EnumType.STRING)
    private TrasactionType trasactionType;

    private BigDecimal amount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Counterparty counterparty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Counterparty finalCounterparty;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Account targetAccount;

    private String merchant;

    @Enumerated(EnumType.STRING)
    private DraftMatchStatusEnum matchStatus;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TransactionMatcher> matchRules;

    private Boolean isModified;

    @Column(columnDefinition = "TEXT")
    private String rawPayload;

    @Column(columnDefinition = "TEXT")
    private String lockedFields;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 软删除标记
    private Boolean isDeleted = false;

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
