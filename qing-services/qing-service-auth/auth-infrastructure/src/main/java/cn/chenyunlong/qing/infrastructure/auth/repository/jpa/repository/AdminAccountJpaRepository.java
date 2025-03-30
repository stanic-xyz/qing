package cn.chenyunlong.qing.infrastructure.auth.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity.AdminAccountEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountJpaRepository extends BaseJpaQueryRepository<AdminAccountEntity, Long> {

}
