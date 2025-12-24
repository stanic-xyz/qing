package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

public record UserConnectionAdded(UserId aggregateId, UserConnection userConnection) {
}
