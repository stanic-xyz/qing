package cn.chenyunlong.qing.auth.domain.user.valueObject;

import cn.chenyunlong.qing.domain.common.Identifiable;
import cn.hutool.core.util.IdUtil;

public record UserId(Long id) implements Identifiable<Long> {

    public UserId {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("用户ID必须大于0");
        }
    }

    public static UserId generate() {
        return new UserId(IdUtil.getSnowflakeNextId());
    }

    public static UserId of(Long id) {
        return new UserId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return identifiableEquals(obj);
    }
}
