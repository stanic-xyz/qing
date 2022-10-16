package cn.chenyunlong.zhangli.model.params;

import cn.chenyunlong.zhangli.model.entities.User;
import cn.chenyunlong.zhangli.model.dto.base.InputConverter;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 注册用户的参数信息
 *
 * @author Stan
 */
@Data
public class UserParam implements InputConverter<User> {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名的字符长度不能超过 {max}")
    private String username;

    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 255, message = "用户昵称的字符长度不能超过 {max}")
    private String nickname;

    @Email(message = "电子邮件地址的格式不正确")
    @NotBlank(message = "电子邮件地址不能为空")
    @Size(max = 127, message = "电子邮件的字符长度不能超过 {max}")
    private String email;

    @Null
    @Size(min = 8, max = 100, message = "密码的字符长度必须在 {min} - {max} 之间")
    private String password;

    @Size(max = 1023, message = "头像链接地址的字符长度不能超过 {max}")
    private String avatar;

    @Size(max = 1023, message = "用户描述的字符长度不能超过 {max}")
    private String description;

}
