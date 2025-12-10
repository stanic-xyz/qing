package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

public record UserDeleted(UserId id, String username) implements UserEvent {
}
