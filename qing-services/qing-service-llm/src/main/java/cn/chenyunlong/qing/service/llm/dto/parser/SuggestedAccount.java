package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

/**
 * 建议账户
 */
@Data
public class SuggestedAccount {
    private String suggestedName;
    private String suggestedType;
    private String reason;
    private String[] sampleDescriptions;
    private String sourceStrategy;

    public SuggestedAccount() {}

    public SuggestedAccount(String suggestedName, String reason, String sourceStrategy) {
        this.suggestedName = suggestedName;
        this.reason = reason;
        this.sourceStrategy = sourceStrategy;
    }
}
