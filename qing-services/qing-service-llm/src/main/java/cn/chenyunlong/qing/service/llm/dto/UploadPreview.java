package cn.chenyunlong.qing.service.llm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UploadPreview {
    private String uploadId;
    private List<PreviewRecordDTO> previewRecords;
    private long totalCount;
    private boolean hasMore;
    private String status; // UPLOADED, MATCHING, IMPORTED
}
