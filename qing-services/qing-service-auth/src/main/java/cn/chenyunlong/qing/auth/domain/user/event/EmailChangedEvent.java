package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

import java.util.Objects;

public final class EmailChangedEvent extends DomainEvent {
    private final UserId id;
    private final Email newEmail;

    public EmailChangedEvent(Object source, UserId id, Email newEmail) {
        super(source);
        this.id = id;
        this.newEmail = newEmail;
    }

    public UserId id() {return id;}

    public Email newEmail() {return newEmail;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (EmailChangedEvent) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.newEmail, that.newEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, newEmail);
    }

    @Override
    public String toString() {
        return "EmailChangedEvent[" +
                "id=" + id + ", " +
                "newEmail=" + newEmail + ']';
    }

}
