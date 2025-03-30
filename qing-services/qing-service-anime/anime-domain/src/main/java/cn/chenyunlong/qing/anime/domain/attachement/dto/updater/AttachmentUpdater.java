package cn.chenyunlong.qing.anime.domain.attachement.dto.updater;

import cn.chenyunlong.qing.anime.domain.attachement.Attachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class AttachmentUpdater {


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
        Optional.ofNullable(getFileName()).ifPresent(param::setFileName);
        Optional.ofNullable(getMimeType()).ifPresent(param::setMimeType);
        Optional.ofNullable(getUrl()).ifPresent(param::setPath);
        Optional.ofNullable(getFileSize()).ifPresent(param::setFileSize);
    }

}
