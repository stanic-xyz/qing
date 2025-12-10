package cn.chenyunlong.qing.domain.common;

// 标识接口 - 所有实体都应该实现
public interface Entity<T> {
    T getId();

    Integer getVersion();
}
