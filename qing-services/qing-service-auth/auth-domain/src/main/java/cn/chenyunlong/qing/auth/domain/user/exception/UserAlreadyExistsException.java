package cn.chenyunlong.qing.auth.domain.user.exception;

// 用户相关异常
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
