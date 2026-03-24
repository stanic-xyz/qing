package cn.chenyunlong.qing.service.llm.service.parser;

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

    // 其他公用方法...
}
