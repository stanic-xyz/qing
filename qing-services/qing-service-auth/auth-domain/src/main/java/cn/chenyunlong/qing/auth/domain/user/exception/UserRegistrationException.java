package cn.chenyunlong.qing.auth.domain.user.exception;

import cn.chenyunlong.qing.domain.common.exception.DomainException;

/**
 * 用户注册领域异常
 */
public class UserRegistrationException extends DomainException {

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
