// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.user.user.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.user.user.User;
import cn.chenyunlong.qing.infrastructure.model.dto.base.InputConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema
public class UserRegisterRequest implements Request, InputConverter<User> {

    @NotBlank
    @Schema(title = "username")
    private String username;

    @NotBlank
    @Schema(title = "nickname")
    private String nickname;

    /**
     * md5加密之后的密码
     */
    @NotBlank
    @Schema(title = "password")
    private String password;

    @NotBlank
    @Schema(title = "phone")
    private String phone;

    @NotBlank
    @Schema(title = "email")
    private String email;

    @Schema(title = "avatar")
    private String avatar;

    @Schema(title = "description")
    private String description;

}