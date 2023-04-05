// ---Auto Generated by Qing-Generator ---
package cn.chenyunlong.qing.domain.anime.comment.updater;

import cn.chenyunlong.qing.domain.anime.comment.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class CommentUpdater {
    @Schema(
            title = "Cid",
            description = "cid"
    )
    private Long cid;

    @Schema(
            title = "Username",
            description = "username"
    )
    private String username;

    @Schema(
            title = "Content",
            description = "content"
    )
    private String content;

    @Schema(
            title = "IpAddress",
            description = "ipAddress"
    )
    private String ipAddress;

    private Long id;

    public void updateComment(Comment param) {
        Optional.ofNullable(getCid()).ifPresent(v -> param.setCid(v));
        Optional.ofNullable(getUsername()).ifPresent(v -> param.setUsername(v));
        Optional.ofNullable(getContent()).ifPresent(v -> param.setContent(v));
        Optional.ofNullable(getIpAddress()).ifPresent(v -> param.setIpAddress(v));
    }
}