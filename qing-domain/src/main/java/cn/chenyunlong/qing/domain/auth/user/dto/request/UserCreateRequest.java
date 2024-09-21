package cn.chenyunlong.qing.domain.auth.user.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.auth.user.dto.validator.PasswordConsistency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema
@Data
public class UserCreateRequest implements Request {

    @Schema(title = "用户名", description = "用户名")
    private String username;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Schema(title = "password", description = "password")
    @NotNull
    @PasswordConsistency(message = "两次密码不相同")
    private RegisterPassword password;

    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
    @Schema(title = "手机号", description = "手机号")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(title = "email", description = "email")
    private String email;

    @Schema(title = "description", description = "description")
    private String description;
}
