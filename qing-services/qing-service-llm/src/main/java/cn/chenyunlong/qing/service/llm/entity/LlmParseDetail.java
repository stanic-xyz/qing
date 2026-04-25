package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * LLM 解析详情实体
 */
@Entity
@Table(name = "llm_parse_detail")
@Data
public class LlmParseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parse_record_id", nullable = false)
    private Long parseRecordId;

    @Column(name = "transaction_record_id")
    private Long transactionRecordId;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_type", length = 16)
    private String transactionType;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "counterparty", length = 128)
    private String counterparty;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "payment_method", length = 64)
    private String paymentMethod;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_name", length = 128)
    private String accountName;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "transaction_no", length = 64)
    private String transactionNo;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 128)
    private String categoryName;

    @Column(name = "confidence", precision = 5, scale = 4)
    private BigDecimal confidence;

    @Column(name = "match_note", length = 256)
    private String matchNote;

    @Column(name = "platform_source", length = 64)
    private String platformSource;

    @Column(name = "consumption_type", length = 64)
    private String consumptionType;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "parse_status", length = 16, nullable = false)
    private String parseStatus = "SUCCESS";

    @Column(name = "import_status", length = 16)
    private String importStatus = "PENDING";

    @Column(name = "need_review")
    private Boolean needReview = false;

    @Column(name = "reviewed")
    private Boolean reviewed = false;

    @Column(name = "reviewed_by", length = 64)
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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
