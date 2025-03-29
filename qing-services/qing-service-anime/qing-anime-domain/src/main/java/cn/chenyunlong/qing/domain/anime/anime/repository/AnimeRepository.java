package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.qing.domain.anime.anime.models.Anime;
import cn.chenyunlong.qing.domain.anime.anime.models.AnimeId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface AnimeRepository extends BaseRepository<Anime, AnimeId> {

    /**
     * 根据名称查询
     *
     * @param name 动漫名称
     */
    boolean existsByName(String name);

    /**
     * 保存动漫
     *
     * @param anime 动漫
     */
    Anime save(Anime anime);

    List<Anime> findByIds(List<Long> animeIds);
}
