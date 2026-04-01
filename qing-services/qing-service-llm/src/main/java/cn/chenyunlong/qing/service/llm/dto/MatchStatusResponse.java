package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

@Data
public class MatchStatusResponse {
    private String status; // PROCESSING, COMPLETED, FAILED
    private String errorMsg;
    private UploadPreview preview;
}