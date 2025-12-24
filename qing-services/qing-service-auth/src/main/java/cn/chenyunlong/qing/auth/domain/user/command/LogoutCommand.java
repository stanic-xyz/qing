package cn.chenyunlong.qing.auth.domain.user.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登出命令
 */
@Data
@Schema(description = "用户登出请求")
public class LogoutCommand {

    @Schema(description = "访问令牌")
    private String accessToken;
}
