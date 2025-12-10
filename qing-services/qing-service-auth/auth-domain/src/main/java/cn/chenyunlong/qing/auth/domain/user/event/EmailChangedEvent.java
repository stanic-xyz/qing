package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

public record EmailChangedEvent(UserId id, Email newEmail) {
}
