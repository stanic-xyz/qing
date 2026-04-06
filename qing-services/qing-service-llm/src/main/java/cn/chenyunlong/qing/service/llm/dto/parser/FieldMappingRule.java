package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.util.List;

@Data
public class FieldMappingRule {
    private String targetField;   // 映射的目标字段 (例如: transactionTime, amount, extData.merchantOrderNo)
    private Integer sourceIndex;  // CSV列索引，从0开始
    private String dateFormat;    // 日期格式，如 yyyy-MM-dd HH:mm:ss
    private List<String> cleanRules; // 清洗规则 (如 TRIM, REMOVE_TABS, REMOVE_COMMAS)
    private String defaultValue;  // 默认值
    private String scriptRule;     // 脚本规则，用于复杂的数据转换
    private String scriptLanguage; // 脚本语言，默认 groovy
    private Boolean scriptEnabled;  // 是否启用脚本
}
