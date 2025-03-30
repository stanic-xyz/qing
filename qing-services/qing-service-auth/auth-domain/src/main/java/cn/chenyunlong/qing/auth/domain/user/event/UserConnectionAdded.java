package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.domain.common.AggregateId;

public record UserConnectionAdded(AggregateId aggregateId, UserConnection qingAuthUser) {
}
