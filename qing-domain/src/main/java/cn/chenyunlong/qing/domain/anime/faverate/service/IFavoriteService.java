package cn.chenyunlong.qing.domain.anime.faverate.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.faverate.dto.creator.FavoriteCreator;
import cn.chenyunlong.qing.domain.anime.faverate.dto.query.FavoriteQuery;
import cn.chenyunlong.qing.domain.anime.faverate.dto.updater.FavoriteUpdater;
import cn.chenyunlong.qing.domain.anime.faverate.dto.vo.FavoriteVO;
import org.springframework.data.domain.Page;

public interface IFavoriteService {

    /**
     * create
     */
    Long createFavorite(FavoriteCreator creator);

    /**
     * update
     */
    void updateFavorite(FavoriteUpdater updater);

    void validFavorite(Long id);

    void invalidFavorite(Long id);

    /**
     * findById
     */
    FavoriteVO findById(Long id);

    /**
     * findByPage
     */
    Page<FavoriteVO> findByPage(PageRequestWrapper<FavoriteQuery> query);
}
