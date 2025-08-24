package cn.chenyunlong.qing.domain.common;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import lombok.Getter;

import java.io.Serializable;

/**
 * 聚合根Id
 */
@Getter
public class AggregateId extends EntityId<Long> implements Serializable {

    private Long id;

    public AggregateId(Long id) {
        Assert.notNull(id, "聚合根id不能为空");
        this.id = id;
    }

    public Long getValue() {
        return this.id;
    }

    protected AggregateId() {
    }

    public static AggregateId generate() {
        return new AggregateId(IdUtil.getSnowflakeNextId());
    }

}
