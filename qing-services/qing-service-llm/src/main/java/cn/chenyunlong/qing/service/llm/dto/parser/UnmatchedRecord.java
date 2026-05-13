package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 无法解析的记录
 */
@Data
public class UnmatchedRecord {
    private int index;
    private String originalText;
    private String reason;
    private BigDecimal amount;
    private String counterparty;

    public UnmatchedRecord() {}

    public UnmatchedRecord(int index, String originalText, String reason) {
        this.index = index;
        this.originalText = originalText;
        this.reason = reason;
    }
}
