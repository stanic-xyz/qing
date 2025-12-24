package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PlatformEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformJpaRepository extends BaseJpaRepository<PlatformEntity, Long> {

}
