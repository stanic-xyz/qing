package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * LLM 解析详情 DTO
 */
@Data
public class LlmParseDetailDTO {
    private Long id;
    private Long parseRecordId;
    private Long transactionRecordId;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionTime;
    private String counterparty;
    private String description;
    private String paymentMethod;
    private Long accountId;
    private String accountName;
    private String status;
    private String transactionNo;
    private Long categoryId;
    private String categoryName;
    private BigDecimal confidence;
    private String matchNote;
    private String platformSource;
    private String consumptionType;
    /**
     * 标签列表（JSON字符串）
     */
    private String tags;
    private String parseStatus;
    private String importStatus;
    private Boolean needReview;
    private Boolean reviewed;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String originalText;
    private Integer version;
}
