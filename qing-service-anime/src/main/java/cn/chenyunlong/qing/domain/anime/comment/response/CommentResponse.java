// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.anime.comment.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CommentResponse extends AbstractJpaResponse {
    @Schema(
            title = "cid"
    )
    private Long cid;

    @Schema(
            title = "username"
    )
    private String username;

    @Schema(
            title = "content"
    )
    private String content;

    @Schema(
            title = "ipAddress"
    )
    private String ipAddress;
}