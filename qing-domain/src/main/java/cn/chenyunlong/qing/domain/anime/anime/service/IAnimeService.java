package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;

public interface IAnimeService {

    /**
     * create
     */
    Long createAnime(AnimeCreateContext createContext);

    /**
     * update
     */
    void updateAnime(AnimeUpdater updater);

    /**
     * valid
     */
    void validAnime(Long id);

    /**
     * invalid
     */
    void invalidAnime(Long id);

    /**
     * 根据Id移除记录
     */
    void removeById(Long id);

}
