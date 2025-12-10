package cn.chenyunlong.qing.domain.common;

import java.time.LocalDateTime;

// 软删除接口
public interface SoftDeletable {
    boolean isDeleted();

    void markAsDeleted();

    LocalDateTime getDeletedAt();
}
