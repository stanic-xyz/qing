package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AnimeJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimeRepositoryImpl extends JpaServiceImpl<AnimeJpaRepository, Anime, Long> implements AnimeRepository {

}
