package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionDirectionTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
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

    // 账单顺序
    private Integer orderNo;

    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    private TransactionDirectionTypeEnum direction;

    private String summary;

    @Enumerated(EnumType.STRING)
    private TransactionType trasactionType;

    // 交易金额
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    // 交易余额
    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Counterparty counterparty;

    private String counterpartyStr;

    // 交易备注
    private String detail;

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
