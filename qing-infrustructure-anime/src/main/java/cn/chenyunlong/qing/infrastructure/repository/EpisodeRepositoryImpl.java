package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.EpisodeJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EpisodeRepositoryImpl extends JpaServiceImpl<EpisodeJpaRepository, Episode, Long> implements EpisodeRepository {

}
