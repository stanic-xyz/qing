package cn.chenyunlong.qing.domain.common;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;

import java.io.Serializable;

/**
 * 聚合根Id
 */
public class AggregateId implements Serializable {

    private Long aggregateId;

    public AggregateId(Long aggregateId) {
        Assert.notNull(aggregateId, "聚合根id不能为空");
        this.aggregateId = aggregateId;
    }

    protected AggregateId() {
    }

    public static AggregateId generate() {
        return new AggregateId(IdUtil.getSnowflakeNextId());
    }

    public Long getId() {
        return aggregateId;
    }

}
