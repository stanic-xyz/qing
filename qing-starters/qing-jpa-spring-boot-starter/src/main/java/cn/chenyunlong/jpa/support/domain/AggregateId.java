package cn.chenyunlong.jpa.support.domain;

import cn.hutool.core.lang.Assert;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

/**
 * 聚合根Id
 */
@Embeddable
public class AggregateId implements Serializable {

    private String aggregateId;

    public AggregateId(String aggregateId) {
        Assert.notBlank(aggregateId, "聚合根id不能为空");
        this.aggregateId = aggregateId;
    }

    protected AggregateId() {
    }

    public static AggregateId generate() {
        return new AggregateId(UUID.randomUUID().toString());
    }

    public String getId() {
        return aggregateId;
    }

    @Override
    public int hashCode() {
        return aggregateId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AggregateId other = (AggregateId) obj;
        if (aggregateId == null) {
            return other.aggregateId == null;
        } else {
            return aggregateId.equals(other.aggregateId);
        }
    }

    @Override
    public String toString() {
        return aggregateId;
    }
}
