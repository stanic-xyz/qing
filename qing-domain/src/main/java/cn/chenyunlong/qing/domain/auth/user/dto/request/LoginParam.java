package cn.chenyunlong.qing.domain.auth.user.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "表单登录参数")
public class LoginParam implements Request {

    @Schema(title = "用户名", description = "用户名")
    private String username;

    @Schema(title = "密码", description = "密码")
    private String password;
}
