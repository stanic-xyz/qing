package cn.chenyunlong.qing.auth.domain.rbac;

import lombok.Value;

import java.time.Instant;

@Value
public class Operator {
    Long userId;
    String username;
    Instant operationTime;

    public static Operator of(Long userId, String username) {
        return new Operator(userId, username, Instant.now());
    }

    public static Operator system() {
        return new Operator(0L, "system", Instant.now());
    }
}
