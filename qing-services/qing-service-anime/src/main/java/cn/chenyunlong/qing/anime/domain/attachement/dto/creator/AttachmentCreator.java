package cn.chenyunlong.qing.anime.domain.attachement.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentCreator {

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

    @Schema(title = "文件路径")
    private String path;

}
