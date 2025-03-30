package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.TypeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeJpaRepository extends BaseJpaRepository<TypeEntity, Long> {

}
