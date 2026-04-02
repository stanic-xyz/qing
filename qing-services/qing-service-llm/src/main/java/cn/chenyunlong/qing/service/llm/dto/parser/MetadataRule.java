package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

@Data
public class MetadataRule {
    private String targetField; // 映射的目标字段 (例如: startTime, endTime, recordCount)
    private Integer rowIndex;   // 所在的行索引
    private Integer colIndex;   // 所在的列索引
    private String regex;       // 提取正则表达式
}