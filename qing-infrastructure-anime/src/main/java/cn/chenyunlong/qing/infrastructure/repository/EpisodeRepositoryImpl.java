package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.EpisodeJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpisodeRepositoryImpl extends JpaServiceImpl<EpisodeJpaRepository, Episode, Long> implements EpisodeRepository {

    private final EpisodeJpaRepository episodeJpaRepository;

    @Override
    public Integer findMaxEpisodeNumberByPlayListId(Long playListId) {
        return episodeJpaRepository.findMaxEpisodeNumberByPlayListId(playListId);
    }

    @Override
    public List<Episode> listByAnimeId(Long animeId) {
        return episodeJpaRepository.listByAnimeId(animeId);
    }
}
