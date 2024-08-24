package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeJpaRepository extends BaseJpaRepository<Episode, Long> {

    @Query("select max(e.episodeNumber) from Episode e where e.playListId = ?1")
    Integer findMaxEpisodeNumberByPlayListId(Long playListId);
}
