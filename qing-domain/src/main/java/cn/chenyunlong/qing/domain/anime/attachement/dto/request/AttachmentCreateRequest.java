package cn.chenyunlong.qing.domain.anime.attachement.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String path;

    @Schema(
        title = "fileSize",
        description = "文件大小"
    )
    private Long fileSize;

}
