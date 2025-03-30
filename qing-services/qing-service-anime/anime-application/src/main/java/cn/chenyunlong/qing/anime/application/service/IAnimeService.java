package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;

public interface IAnimeService {

    /**
     * create
     */
    Anime createAnime(AnimeCreator creator);

    /**
     * update
     */
    void updateAnime(AnimeUpdater updater);

    void validAnime(Long id);

    void invalidAnime(Long id);

    /**
     * 根据Id移除记录
     */
    void removeById(Long id);
}
