package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import java.util.Optional;

public interface AnimeRepository extends BaseRepository<Anime, Long> {


    Optional<Anime> findById(Long animeId);

    /**
     * 根据名称查询
     *
     * @param name 动漫名称
     */
    Integer existsByName(String name);
}
