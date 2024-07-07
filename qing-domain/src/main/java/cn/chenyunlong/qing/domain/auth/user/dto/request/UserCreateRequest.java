package cn.chenyunlong.qing.domain.auth.user.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.auth.user.controller.validator.PasswordConsistency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class UserCreateRequest implements Request {

    @Schema(title = "username", description = "用户名")
    private String username;

    @Schema(title = "password", description = "password")
    @NotNull
    @PasswordConsistency(message = "两次密码不相同")
    private RegisterPassword password;

    @Schema(title = "手机号", description = "手机号")
    private String phone;

    @Schema(title = "email", description = "email")
    private String email;

    @Schema(title = "description", description = "description")
    private String description;
}
