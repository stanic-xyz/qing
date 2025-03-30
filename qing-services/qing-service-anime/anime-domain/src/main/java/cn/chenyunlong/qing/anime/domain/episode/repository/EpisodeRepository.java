package cn.chenyunlong.qing.anime.domain.episode.repository;

import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface EpisodeRepository extends BaseRepository<Episode, AggregateId> {

    Integer findMaxEpisodeNumberByPlayListId(Long playListId);

    List<Episode> listByAnimeId(Long animeId);
}
