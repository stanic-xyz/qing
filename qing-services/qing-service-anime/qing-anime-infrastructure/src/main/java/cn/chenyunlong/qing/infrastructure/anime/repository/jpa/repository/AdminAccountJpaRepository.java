package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth.AdminAccountEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountJpaRepository extends BaseJpaQueryRepository<AdminAccountEntity, Long> {

}
