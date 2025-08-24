package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;

public record UserConnectionAdded(QingUserId aggregateId, UserConnection userConnection) {
}
