package cn.chenyunlong.qing.domain.auth.user.dto.response;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema
public class UserResponse extends AbstractJpaResponse {


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
        title = "expireTime",
        description = "expireTime"
    )
    private LocalDateTime expireTime;

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
