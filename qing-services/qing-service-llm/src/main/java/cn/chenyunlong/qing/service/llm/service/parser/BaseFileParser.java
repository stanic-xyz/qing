package cn.chenyunlong.qing.service.llm.service.parser;

import cn.chenyunlong.qing.service.llm.dto.parser.FileMetadata;
import cn.chenyunlong.qing.service.llm.dto.parser.ParseResult;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class BaseFileParser implements FileParser {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected LocalDateTime parseDateTime(String str, String pattern) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
    }

    protected BigDecimal parseAmount(String str) {
        // 去除逗号、人民币符号等
        String clean = str.replaceAll("[^\\d.-]", "");
        return new BigDecimal(clean);
    }

    protected ParseResult wrapResult(List<TransactionRecord> records) {
        LocalDateTime minTime = null;
        LocalDateTime maxTime = null;

        for (TransactionRecord record : records) {
            // 自动推算资金类型 (fundType)
            if (record.getFundType() == null && record.getFundSource() != null) {
                record.setFundType(deduceFundType(record.getFundSource()));
            }

            if (record.getTransactionTime() != null) {
                if (minTime == null || record.getTransactionTime().isBefore(minTime)) {
                    minTime = record.getTransactionTime();
                }
                if (maxTime == null || record.getTransactionTime().isAfter(maxTime)) {
                    maxTime = record.getTransactionTime();
                }
            }
        }

        FileMetadata metadata = FileMetadata.builder()
                .startTime(minTime)
                .endTime(maxTime)
                .recordCount(records.size())
                .build();

        return new ParseResult(metadata, records);
    }

    /**
     * 根据资金来源描述，自动推断属于内部资金还是外部资金
     */
    protected String deduceFundType(String fundSource) {
        if (fundSource == null || fundSource.isEmpty()) {
            return null;
        }
        String lower = fundSource.toLowerCase();
        // 内部资金关键词
        if (lower.contains("余额") || lower.contains("零钱") || lower.contains("钱包") || lower.contains("花呗")) {
            return "INTERNAL";
        }
        // 外部资金关键词
        if (lower.contains("银行") || lower.contains("信用") || lower.contains("储蓄") || lower.contains("网商") || lower.contains("卡")) {
            return "EXTERNAL";
        }
        return "UNKNOWN";
    }
}
