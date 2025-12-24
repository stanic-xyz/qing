package cn.chenyunlong.qing.auth.domain.user.dto.creator;

import cn.chenyunlong.common.enums.MFAType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserCreator {

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

    @Schema(
        title = "password",
        description = "password"
    )
    private String password;

    @Schema(
        title = "phone",
        description = "phone"
    )
    private String phone;

    @Schema(
        title = "email",
        description = "email"
    )
    private String email;

    @Schema(
        title = "avatar",
        description = "avatar"
    )
    private String avatar;

    @Schema(
        title = "description",
        description = "description"
    )
    private String description;

    @Schema(
        title = "mfaType",
        description = "mfaType"
    )
    private MFAType mfaType;

    @Schema(
        title = "mfaKey",
        description = "mfaKey"
    )
    private String mfaKey;

}
