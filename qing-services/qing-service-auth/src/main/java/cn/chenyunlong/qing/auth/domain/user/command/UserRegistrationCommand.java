package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.PhoneNumber;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.lang.Nullable;


/**
 * 用户注册命令
 */
@Builder(access = AccessLevel.PRIVATE)
public record UserRegistrationCommand(Username username, String password, Email email, @Nullable PhoneNumber phoneNumber, @Nullable String nickname) {

    public UserRegistrationCommand(Username username, String password, Email email, PhoneNumber phoneNumber, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        validate();
    }

    public static UserRegistrationCommand create(String username, String password, String email, String phoneNumber, String nickname) {
        return UserRegistrationCommand.builder()
                .username(Username.of(username))
                .email(Email.of(email))
                .password(password)
                .phoneNumber(PhoneNumber.of(phoneNumber))
                .nickname(nickname)
                .build();
    }

    private void validate() {
        if (password == null) {
            throw new IllegalArgumentException("密码长度至少6位");
        }
    }
}
