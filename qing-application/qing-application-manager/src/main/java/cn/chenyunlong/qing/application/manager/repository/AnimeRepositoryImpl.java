package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.AnimeJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimeRepositoryImpl extends JpaServiceImpl<AnimeJpaRepository, Anime, Long> implements AnimeRepository {

}
