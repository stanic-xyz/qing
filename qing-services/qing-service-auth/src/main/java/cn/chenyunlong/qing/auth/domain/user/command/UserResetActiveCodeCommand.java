package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;

public record UserResetActiveCodeCommand(Username username) {

    public static UserResetActiveCodeCommand create(Username username) {
        return new UserResetActiveCodeCommand(username);
    }
}
