package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AnimeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeRepositoryImpl extends JpaServiceImpl<AnimeJpaRepository, Anime, Long> implements AnimeRepository {


    private final AnimeJpaRepository animeJpaRepository;

    /**
     * 根据名称查询
     *
     * @param name 动漫名称
     */
    @Override
    public Integer existsByName(String name) {
        return animeJpaRepository.existsByName(name);
    }
}
