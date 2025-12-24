package cn.chenyunlong.qing.auth.domain.user.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "表单登录参数")
public class LoginParam implements Request {

    @NotBlank(message = "用户名不能为空")
    @Schema(title = "用户名", description = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(title = "密码", description = "密码")
    private String password;
}
