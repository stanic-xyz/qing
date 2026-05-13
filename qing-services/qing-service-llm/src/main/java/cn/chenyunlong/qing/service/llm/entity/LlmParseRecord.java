package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * LLM 解析记录实体
 */
@Entity
@Table(name = "llm_parse_record")
@Data
public class LlmParseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "upload_id", length = 64)
    private String uploadId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_hash", nullable = false, length = 64)
    private String fileHash;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "parser_mode", length = 16)
    private String parserMode = "LLM";

    @Column(name = "category_strategy", length = 32)
    private String categoryStrategy = "BY_CONSUMPTION_TYPE";

    @Column(name = "forced_category_id")
    private Long forcedCategoryId;

    @Column(name = "task_id", length = 64)
    private String taskId;

    @Column(name = "task_status", length = 16)
    private String taskStatus = "PENDING";

    @Column(name = "progress")
    private Integer progress = 0;

    @Column(name = "total_records")
    private Integer totalRecords = 0;

    @Column(name = "success_count")
    private Integer successCount = 0;

    @Column(name = "failed_count")
    private Integer failedCount = 0;

    @Column(name = "need_review_count")
    private Integer needReviewCount = 0;

    @Column(name = "low_confidence_count")
    private Integer lowConfidenceCount = 0;

    @Column(name = "input_tokens")
    private Integer inputTokens = 0;

    @Column(name = "output_tokens")
    private Integer outputTokens = 0;

    @Column(name = "estimated_cost", precision = 10, scale = 4)
    private java.math.BigDecimal estimatedCost = java.math.BigDecimal.ZERO;

    @Column(name = "cache_key", length = 128)
    private String cacheKey;

    @Column(name = "cache_expired_at")
    private LocalDateTime cacheExpiredAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "last_retry_at")
    private LocalDateTime lastRetryAt;

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
