package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeJpaRepository extends BaseJpaRepository<Episode, Long> {

}
