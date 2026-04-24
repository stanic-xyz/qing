package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统一中间格式 - 账单记录
 */
@Data
public class CommonBillRecord {
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionTime;
    private String counterparty;
    private String description;
    private String paymentMethod;
    private String accountId;
    private String accountName;
    private String status;
    private String transactionNo;
    private String platformSource;
    private String consumptionType;
    private Long categoryId;
    private String categoryName;
    private BigDecimal confidence;
    private String matchNote;
    private boolean needNewCategory;
}
