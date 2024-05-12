package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeJpaRepository extends BaseJpaRepository<Anime, Long> {

}
