package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.EpisodeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeJpaRepository extends BaseJpaRepository<EpisodeEntity, Long> {

    @Query("select max(e.episodeNumber) from EpisodeEntity e where e.playListId = ?1")
    Integer findMaxEpisodeNumberByPlayListId(Long playListId);

    @Query("select e from EpisodeEntity e where e.animeId = ?1 ")
    List<Episode> listByAnimeId(Long animeId);
}
