package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新用户请求DTO
 */
@Data
@Schema(description = "更新用户请求")
public class UpdateUserRequest {

    @Schema(description = "昵称", example = "测试用户")
    @JsonProperty("nickname")
    private String nickname;

    @Schema(description = "头像URL")
    @JsonProperty("avatar")
    private String avatar;

    @Schema(description = "手机号", example = "13800138000")
    @JsonProperty("phone")
    private String phone;

    @Schema(description = "邮箱", example = "test@example.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "用户描述")
    @JsonProperty("description")
    private String description;
}
