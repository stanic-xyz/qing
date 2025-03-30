package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.domain.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.EpisodeJpaRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpisodeRepositoryImpl implements EpisodeRepository {

    private final EpisodeJpaRepository episodeJpaRepository;

    @Override
    public Integer findMaxEpisodeNumberByPlayListId(Long playListId) {
        return episodeJpaRepository.findMaxEpisodeNumberByPlayListId(playListId);
    }

    @Override
    public List<Episode> listByAnimeId(Long animeId) {
        return episodeJpaRepository.listByAnimeId(animeId);
    }

    @Override
    public Episode save(Episode entity) {
        return null;
    }

    @Override
    public Optional<Episode> findById(AggregateId id) {
        return Optional.empty();
    }
}
