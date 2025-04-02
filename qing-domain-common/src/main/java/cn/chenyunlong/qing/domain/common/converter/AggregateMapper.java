package cn.chenyunlong.qing.domain.common.converter;

import cn.chenyunlong.qing.domain.common.AggregateId;

import java.io.Serializable;

/**
 * 聚合根Id
 */
public class AggregateMapper implements Serializable {


    public AggregateId generate(Long aggregateId) {
        return new AggregateId(aggregateId);
    }

    public Long getId(AggregateId aggregateId) {
        if (aggregateId != null) {
            return aggregateId.getId();
        }
        return null;
    }

}
