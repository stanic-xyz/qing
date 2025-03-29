package cn.chenyunlong.qing.domain.anime.anime.exception;

public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException(String userId) {
        super("用户未激活: " + userId);
    }
}
