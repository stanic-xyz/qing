package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.models.Anime;

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

    /**
     * 根据Id查询
     */
    AnimeVO findById(Long id);

}
