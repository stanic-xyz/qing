package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 解析汇总信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseSummary {
    private int totalRecords;
    private int successCount;
    private int failedCount;
    private int needReviewCount;
    private double avgConfidence;
    private int inputTokens;
    private int outputTokens;
    private double estimatedCost;
    private List<String> platformSources;
    private List<String> consumptionTypes;
}
