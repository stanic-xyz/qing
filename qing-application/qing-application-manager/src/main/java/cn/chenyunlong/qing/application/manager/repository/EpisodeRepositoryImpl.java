package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.EpisodeJpaRepository;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.repository.EpisodeRepository;
import org.springframework.stereotype.Service;

@Service
public class EpisodeRepositoryImpl extends JpaServiceImpl<EpisodeJpaRepository, Episode, Long> implements EpisodeRepository {

}
