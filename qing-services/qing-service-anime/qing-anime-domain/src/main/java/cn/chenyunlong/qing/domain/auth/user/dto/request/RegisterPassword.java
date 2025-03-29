package cn.chenyunlong.qing.domain.auth.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class RegisterPassword {

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPass;

}
