package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "认证令牌", required = true)
    private String token;

    @Schema(description = "用户ID", required = true)
    private Long userId;

    @Schema(description = "用户名", required = true)
    private String username;

    @Schema(description = "用户头像")
    private String avatar;
}