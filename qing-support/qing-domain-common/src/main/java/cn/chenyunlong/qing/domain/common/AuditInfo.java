package cn.chenyunlong.qing.domain.common;


import org.springframework.expression.spel.ast.Operator;

import java.time.Instant;
import java.util.Objects;

/**
 * 审计信息值对象
 */
public record AuditInfo(String createdBy, Instant createdAt, String updatedBy, Instant updatedAt) {
    // 静态工厂方法
    public static AuditInfo create(String createdBy) {
        return new AuditInfo(createdBy, Instant.now(), createdBy, Instant.now());
    }

    public static AuditInfo createSystem() {
        return create("system");
    }

    public static AuditInfo restore(String createBy, Instant createdAt, String updatedBy, Instant updatedAt) {
        return new AuditInfo(createBy, createdAt, updatedBy, updatedAt);
    }

    public AuditInfo update(String updatedBy) {
        return new AuditInfo(this.createdBy, this.createdAt, updatedBy, Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AuditInfo(String by, Instant at, String updated, Instant updatedAt1)) {
            return Objects.equals(createdBy, by) &&
                    Objects.equals(createdAt, at) &&
                    Objects.equals(updatedBy, updated) &&
                    Objects.equals(updatedAt, updatedAt1);
        }
        return false;
    }

}
