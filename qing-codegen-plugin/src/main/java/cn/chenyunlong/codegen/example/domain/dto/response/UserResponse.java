package cn.chenyunlong.codegen.example.domain.dto.response;

import cn.chenyunlong.codegen.example.domain.User;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class UserResponse extends AbstractJpaResponse {
    @Schema(
            title = "username"
    )
    private String username;

    @Schema(
            title = "email"
    )
    private String email;

    @Schema(
            title = "realName"
    )
    private String realName;

    @Schema(
            title = "phoneNumber"
    )
    private String phoneNumber;

    @Schema(
            title = "status"
    )
    private Integer status;

    @Schema(
            title = "role"
    )
    private User.UserRole role;

    @Schema(
            title = "lastLoginTime"
    )
    private LocalDateTime lastLoginTime;

    @Schema(
            title = "avatarUrl"
    )
    private String avatarUrl;

    @Schema(
            title = "bio"
    )
    private String bio;
}
