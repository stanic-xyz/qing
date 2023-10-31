package cn.chenyunlong.qing.domain.attachement.dto.updater;

import cn.chenyunlong.qing.domain.attachement.Attachment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class AttachmentUpdater {
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

    private Long id;

    public void updateAttachment(Attachment param) {
        Optional.ofNullable(getFileId()).ifPresent(param::setFileId);
        Optional.ofNullable(getFileName()).ifPresent(param::setFileName);
        Optional.ofNullable(getMimeType()).ifPresent(param::setMimeType);
        Optional.ofNullable(getUrl()).ifPresent(param::setUrl);
        Optional.ofNullable(getFileSize()).ifPresent(param::setFileSize);
    }

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
