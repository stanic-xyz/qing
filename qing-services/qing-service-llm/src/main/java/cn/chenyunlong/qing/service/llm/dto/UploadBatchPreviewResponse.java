package cn.chenyunlong.qing.service.llm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UploadBatchPreviewResponse {
    private String uploadId;
    private String fileName;
    private int parsedCount;
    private List<PreviewRecordDTO> previewRecords;
}