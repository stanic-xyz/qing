package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

/**
 * 建议分类
 */
@Data
public class SuggestedCategory {
    /**
     * 建议的分类名称
     */
    private String name;

    /**
     * 父分类ID（顶级为null）
     */
    private Long parentId;

    /**
     * 分类类型（可选，用于扩展）
     */
    private String type;

    /**
     * 生成原因
     */
    private String reason;

    /**
     * 样本描述列表（JSON字符串，存储时序列化）
     */
    private String sampleDescriptions;

    /**
     * 策略来源：BY_CONSUMPTION_TYPE / BY_PLATFORM / HYBRID
     */
    private String sourceStrategy;

    public SuggestedCategory() {}

    public SuggestedCategory(String name, Long parentId, String reason, String sourceStrategy) {
        this.name = name;
        this.parentId = parentId;
        this.reason = reason;
        this.sourceStrategy = sourceStrategy;
    }
}
