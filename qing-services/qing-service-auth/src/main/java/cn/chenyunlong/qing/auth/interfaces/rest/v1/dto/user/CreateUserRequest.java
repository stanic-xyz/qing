package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建用户请求DTO
 */
@Data
@Schema(description = "创建用户请求")
public class CreateUserRequest {

    @Schema(description = "用户名", example = "testuser")
    @JsonProperty("username")
    private String username;

    @Schema(description = "密码", example = "password123")
    @JsonProperty("password")
    private String password;

    @Schema(description = "昵称", example = "测试用户")
    @JsonProperty("nickname")
    private String nickname;

    @Schema(description = "邮箱", example = "test@example.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    @JsonProperty("phone")
    private String phone;

    @Schema(description = "头像URL")
    @JsonProperty("avatar")
    private String avatar;

    @Schema(description = "用户描述")
    @JsonProperty("description")
    private String description;
}
