package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountPlatformRelJpaRepository extends BaseJpaRepository<AdminAccountPlatformRel, Long> {

}
