package cn.chenyunlong.qing.application.manager.jimmer.filters;

import org.babyfish.jimmer.sql.MappedSuperclass;

@MappedSuperclass
public interface TenantAware {

    String tenant();
}
