package cn.chenyunlong.qing.auth.domain.user.command;


import cn.chenyunlong.qing.auth.domain.user.dto.entity.QingAuthUser;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;

public record RegisterConnectionCommand(Username username, QingAuthUser qingAuthUser) {
    public static RegisterConnectionCommand create(Username username, QingAuthUser qingAuthUser) {
        return new RegisterConnectionCommand(username, qingAuthUser);
    }
}
