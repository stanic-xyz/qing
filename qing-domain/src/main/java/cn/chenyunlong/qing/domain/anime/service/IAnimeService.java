package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.dto.vo.AnimeVO;
import org.springframework.data.domain.Page;

public interface IAnimeService {
    /**
     * create
     */
    Long createAnime(AnimeCreator creator);

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
     * findById
     */
    AnimeVO findById(Long id);

    /**
     * findByPage
     */
    Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> query);
}
