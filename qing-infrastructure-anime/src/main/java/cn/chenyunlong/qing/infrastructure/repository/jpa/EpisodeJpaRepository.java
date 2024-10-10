package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeJpaRepository extends BaseJpaRepository<Episode, Long> {

    @Query("select max(e.episodeNumber) from Episode e where e.playListId = ?1")
    Integer findMaxEpisodeNumberByPlayListId(Long playListId);

    @Query("select e from Episode e where e.animeId = ?1 ")
    List<Episode> listByAnimeId(Long animeId);
}
