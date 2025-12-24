package cn.chenyunlong.qing.auth.domain.user.exception;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String userId) {
        super("用户未激活: " + userId);
    }
}

