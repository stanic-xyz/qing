package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

import java.util.Objects;

public final class UserActivated extends DomainEvent {
    private final UserId aggregateId;

    public UserActivated(Object source, UserId aggregateId) {
        super(source);
        this.aggregateId = aggregateId;
    }

    public UserId aggregateId() {return aggregateId;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserActivated) obj;
        return Objects.equals(this.aggregateId, that.aggregateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateId);
    }

    @Override
    public String toString() {
        return "UserActivated[" +
                "aggregateId=" + aggregateId + ']';
    }

}
