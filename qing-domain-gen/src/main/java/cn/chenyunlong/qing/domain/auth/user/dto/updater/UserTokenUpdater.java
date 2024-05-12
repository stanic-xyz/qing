package cn.chenyunlong.qing.domain.auth.user.dto.updater;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserTokenUpdater {

    @Schema(
        title = "uid",
        description = "用户唯一ID"
    )
    private Long uid;

    @Schema(
        title = "username",
        description = "用户名"
    )
    private String accessToken;

    @Schema(
        title = "nickname",
        description = "昵称"
    )
    private String nickname;

    private Long id;

    public void updateUserToken(UserToken userToken) {
        userToken.setAccessToken(accessToken);
    }
}
