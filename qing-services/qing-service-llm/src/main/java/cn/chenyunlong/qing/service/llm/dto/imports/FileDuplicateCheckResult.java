package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDuplicateCheckResult {
    private boolean duplicate;
    private Long existingFileId;
    private String existingFileName;
    private LocalDateTime existingUploadTime;
}
