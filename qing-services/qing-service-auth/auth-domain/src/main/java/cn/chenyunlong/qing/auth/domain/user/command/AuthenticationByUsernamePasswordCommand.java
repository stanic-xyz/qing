package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.user.valueObject.EncryptedPassword;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationByUsernamePasswordCommand {

    private Username username;

    private EncryptedPassword password;

    private IpAddress clientIpAddress;

    private String userAgent;

    /**
     * 创建用户名密码登录命令（使用明文承载以便进行 BCrypt 校验）
     */
    public static AuthenticationByUsernamePasswordCommand create(String username, String password, String ipAddress, String userAgent) {
        return new AuthenticationByUsernamePasswordCommand(Username.of(username), EncryptedPassword.ofEncrypted(password), IpAddress.of(ipAddress), userAgent);
    }
}
