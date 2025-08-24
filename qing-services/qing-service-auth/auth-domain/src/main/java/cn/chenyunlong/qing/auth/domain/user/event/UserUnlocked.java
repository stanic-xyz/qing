package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.user.QingUserId;

public record UserUnlocked(QingUserId aggregateId) {
}
