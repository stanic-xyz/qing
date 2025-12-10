package cn.chenyunlong.qing.auth.domain.authentication.valueObject;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.domain.common.ValueObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证结果值对象
 */
@Data
public class AuthenticationResult implements ValueObject {

    private final boolean success;
    private final AuthenticationToken token;
    private final User user;
    private final AuthFailureReason failureReason;
    private final String message;
    private final LocalDateTime authenticatedAt;

    private AuthenticationResult(boolean success, AuthenticationToken token, User user,
                                 AuthFailureReason failureReason, String message) {
        this.success = success;
        this.token = token;
        this.user = user;
        this.failureReason = failureReason;
        this.message = message;
        this.authenticatedAt = LocalDateTime.now();
    }

    public static AuthenticationResult success(AuthenticationToken token, User user) {
        return new AuthenticationResult(true, token, user, null, "认证成功");
    }

    public static AuthenticationResult failure(AuthFailureReason reason, String message) {
        return new AuthenticationResult(false, null, null, reason, message);
    }

    public static AuthenticationResult from(Authentication authentication, User user) {
        return AuthenticationResult.success(authentication.getTokenInfo(), user);
    }
}
