package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.platform.Platform;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth.PlatformEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformJpaRepository extends BaseJpaRepository<PlatformEntity, Long> {

}
