package cn.chenyunlong.qing.service.llm.dto.imports;

import cn.chenyunlong.qing.service.llm.enums.FileTypeEnum;
import lombok.Data;

@Data
public class UploadFileResponse {
    private Long fileId;
    private String fileName;
    private String fileHash;
    private Long fileSize;
    private FileTypeEnum fileType;
    private boolean isDuplicate;
    private Long existingFileId;
}
