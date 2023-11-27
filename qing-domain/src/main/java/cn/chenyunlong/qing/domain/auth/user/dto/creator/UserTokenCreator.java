package cn.chenyunlong.qing.domain.auth.user.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserTokenCreator {
    @Schema(
            title = "uid",
            description = "用户唯一ID"
    )
    private Long uid;

    @Schema(
            title = "username",
            description = "用户名"
    )
    private String username;

    @Schema(
            title = "nickname",
            description = "昵称"
    )
    private String nickname;
}
