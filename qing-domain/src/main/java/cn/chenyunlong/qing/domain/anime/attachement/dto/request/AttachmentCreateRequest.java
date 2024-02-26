package cn.chenyunlong.qing.domain.anime.attachement.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AttachmentCreateRequest implements Request {

    @Schema(
        title = "fileId",
        description = "文件ID"
    )
    private Long fileId;

    @Schema(
        title = "fileName",
        description = "文件名称"
    )
    private String fileName;

    @Schema(
        title = "mimeType",
        description = "文件类型"
    )
    private String mimeType;

    @Schema(
        title = "url",
        description = "文件地址"
    )
    private String url;

    @Schema(
        title = "fileSize",
        description = "文件大小"
    )
    private Long fileSize;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
