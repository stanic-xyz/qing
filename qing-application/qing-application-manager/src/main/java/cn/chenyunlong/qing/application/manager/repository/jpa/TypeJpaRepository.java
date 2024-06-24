package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.type.Type;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeJpaRepository extends BaseJpaRepository<Type, Long> {

}
