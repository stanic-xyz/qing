package cn.chenyunlong.qing.domain.anime.attachement.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema
public class AttachmentResponse extends AbstractJpaResponse {

    static {
    }

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

    @Schema(
        title = "文件地址",
        description = "文件地址"
    )
    private String path;

}
