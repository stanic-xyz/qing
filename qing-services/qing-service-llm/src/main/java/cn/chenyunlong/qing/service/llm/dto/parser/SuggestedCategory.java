package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

/**
 * 建议分类
 */
@Data
public class SuggestedCategory {
    private String suggestedName;
    private Long suggestedParentId;
    private String suggestedType;
    private String reason;
    private String[] sampleDescriptions;
    private String sourceStrategy;

    public SuggestedCategory() {}

    public SuggestedCategory(String suggestedName, String reason, String sourceStrategy) {
        this.suggestedName = suggestedName;
        this.reason = reason;
        this.sourceStrategy = sourceStrategy;
    }
}
