package cn.chenyunlong.qing.domain.anime.attachement.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.attachement.Attachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PROTECTED
)
public class AttachmentVO extends AbstractBaseJpaVo {

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
    private String path;

    @Schema(
        title = "fileSize",
        description = "文件大小"
    )
    private Long fileSize;

    public AttachmentVO(Attachment source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setFileId(source.getFileId());
        this.setFileName(source.getFileName());
        this.setMimeType(source.getMimeType());
        this.setPath(source.getPath());
        this.setFileSize(source.getFileSize());
    }

}
