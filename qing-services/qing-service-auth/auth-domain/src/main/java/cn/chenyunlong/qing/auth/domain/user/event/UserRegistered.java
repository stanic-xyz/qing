package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.domain.common.AggregateId;

public record UserRegistered(AggregateId aggregateId) {
}
