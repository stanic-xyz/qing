package cn.chenyunlong.qing.domain.common;

import java.time.Instant;

// 可审计接口
public interface Auditable {
    AuditInfo getAuditInfo();

    void setAuditInfo(AuditInfo auditInfo);

    // 便捷方法
    default Instant getCreatedAt() {
        return getAuditInfo() != null ? getAuditInfo().createdAt() : null;
    }

    default String getCreatedBy() {
        return getAuditInfo() != null ? getAuditInfo().createdBy() : null;
    }
}
