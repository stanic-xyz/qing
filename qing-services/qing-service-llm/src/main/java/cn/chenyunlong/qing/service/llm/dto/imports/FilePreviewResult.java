package cn.chenyunlong.qing.service.llm.dto.imports;

import cn.chenyunlong.qing.service.llm.enums.FileTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class FilePreviewResult {
    private Long fileId;
    private String fileName;
    private FileTypeEnum fileType;
    private List<String> headers;
    private List<List<String>> rows;
    private int totalRows;
    private String downloadUrl;
}
