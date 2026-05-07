package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.enums.FileUploadStatusEnum;
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
    private FileUploadStatusEnum status; // UPLOADED, MATCHING, IMPORTED
}
