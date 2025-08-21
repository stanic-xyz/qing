package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 注册请求DTO
 */
@Data
@Schema(description = "注册请求")
public class RegisterRequest {

    @Schema(description = "用户名", required = true, example = "newuser")
    private String username;

    @Schema(description = "密码", required = true, example = "password")
    private String password;

    @Schema(description = "确认密码", required = true, example = "password")
    private String confirmPassword;

    @Schema(description = "邮箱", required = true, example = "user@example.com")
    private String email;

    @Schema(description = "手机号码", example = "13800138000")
    private String phone;
}