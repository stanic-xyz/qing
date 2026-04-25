package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.util.List;

/**
 * LLM 完整解析响应
 */
@Data
public class LlmParseResponse {
    private boolean success;
    private String taskId;
    private List<CommonBillRecord> records;
    private ParseSummary summary;
    private List<SuggestedCategory> suggestedNewCategories;
    private List<SuggestedAccount> suggestedNewAccounts;
    private List<UnmatchedRecord> unmatchedRecords;
    private String errorMessage;

    public static LlmParseResponse success(List<CommonBillRecord> records, ParseSummary summary) {
        LlmParseResponse response = new LlmParseResponse();
        response.setSuccess(true);
        response.setRecords(records);
        response.setSummary(summary);
        return response;
    }

    public static LlmParseResponse error(String errorMessage) {
        LlmParseResponse response = new LlmParseResponse();
        response.setSuccess(false);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
