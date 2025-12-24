package cn.chenyunlong.qing.auth.domain.user.valueObject;

import lombok.Data;

import java.time.Instant;

@Data
public class Session {
    private String id;
    private UserId userId;
    private Instant expiresAt;
    private String ip;
    private String userAgent;
    private boolean active = true;

    public Session(String id, UserId userId, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isActive() {
        return active && !isExpired();
    }

    public void expire() {
        this.active = false;
    }
}
