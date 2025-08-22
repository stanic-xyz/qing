package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.domain.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.EpisodeConverter;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.EpisodeEntity;
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
    private final EpisodeConverter episodeConverter;

    @Override
    public Integer findMaxEpisodeNumberByPlayListId(Long playListId) {
        return episodeJpaRepository.findMaxEpisodeNumberByPlayListId(playListId);
    }

    @Override
    public List<Episode> listByAnimeId(Long animeId) {
        return episodeJpaRepository.listByAnimeId(animeId)
                .stream()
                .map(episodeConverter::toDomain)
                .toList();
    }

    @Override
    public Episode save(Episode entity) {
        EpisodeEntity episodeEntity = episodeConverter.toEntity(entity);
        EpisodeEntity savedEntity = episodeJpaRepository.saveAndFlush(episodeEntity);
        return episodeConverter.toDomain(savedEntity);
    }

    @Override
    public Optional<Episode> findById(AggregateId id) {
        if (id == null || id.getId() == null) {
            return Optional.empty();
        }
        return episodeJpaRepository.findById(id.getId())
                .map(episodeConverter::toDomain);
    }

}
