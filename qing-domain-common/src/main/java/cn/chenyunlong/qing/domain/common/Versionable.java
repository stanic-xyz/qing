package cn.chenyunlong.qing.domain.common;

// 版本控制接口
public interface Versionable {
    Long getVersion();

    void setVersion(Long version);
}
