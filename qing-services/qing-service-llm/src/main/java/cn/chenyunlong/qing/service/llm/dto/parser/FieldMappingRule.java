package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.util.List;

@Data
public class FieldMappingRule {
    /**
     * 映射的目标字段（例如: transactionTime, amount, extData.merchantOrderNo）
     */
    private String targetField;

    /**
     * CSV 列索引（从 0 开始）
     */
    private Integer sourceIndex;

    /**
     * 日期格式（例如: yyyy-MM-dd HH:mm:ss）
     */
    private String dateFormat;

    /**
     * 清洗规则（例如: TRIM, REMOVE_TABS, REMOVE_COMMAS）
     */
    private List<String> cleanRules;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 脚本内容。
     * <p>
     * 通常脚本返回当前 targetField 的值；为兼容旧版 amount 脚本，也允许返回 Map（key=字段名，value=字段值）。
     */
    private String scriptRule;

    /**
     * 脚本标记：为 true 时才会执行 scriptRule。
     * <p>
     * 兼容旧版：如果 scriptEnabled 为 null 但 scriptRule 不为空，默认视为开启。
     */
    private Boolean scriptEnabled;

    /**
     * 脚本语言：默认 groovy（后续可扩展）。
     */
    private String scriptLanguage;
}
