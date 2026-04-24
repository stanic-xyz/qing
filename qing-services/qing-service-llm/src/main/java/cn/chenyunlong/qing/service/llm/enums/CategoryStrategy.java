package cn.chenyunlong.qing.service.llm.enums;

/**
 * LLM 分类策略枚举
 */
public enum CategoryStrategy {
    /**
     * 按消费类型分类（餐饮/购物/娱乐等）
     */
    BY_CONSUMPTION_TYPE("BY_CONSUMPTION_TYPE", "按消费类型分类"),

    /**
     * 按平台来源分类（京东/美团/抖音等）
     */
    BY_PLATFORM("BY_PLATFORM", "按平台来源分类"),

    /**
     * 混合模式（同时考虑消费类型和平台来源）
     */
    HYBRID("HYBRID", "混合模式");

    private final String code;
    private final String description;

    CategoryStrategy(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CategoryStrategy fromCode(String code) {
        for (CategoryStrategy strategy : values()) {
            if (strategy.code.equals(code)) {
                return strategy;
            }
        }
        return BY_CONSUMPTION_TYPE;
    }
}
