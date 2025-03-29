package cn.chenyunlong.qing.domain.anime.episode.repository;

import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

public interface EpisodeRepository extends BaseRepository<Episode, AggregateId> {

    Integer findMaxEpisodeNumberByPlayListId(Long playListId);

    List<Episode> listByAnimeId(Long animeId);
}
