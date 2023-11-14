package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.dto.vo.AnimeCategoryVO;
import org.springframework.data.domain.Page;

public interface IAnimeCategoryService {
    /**
     * create
     */
    Long createAnimeCategory(AnimeCategoryCreator creator);

    /**
     * update
     */
    void updateAnimeCategory(AnimeCategoryUpdater updater);

    /**
     * valid
     */
    void validAnimeCategory(Long id);

    /**
     * invalid
     */
    void invalidAnimeCategory(Long id);

    /**
     * findById
     */
    AnimeCategoryVO findById(Long id);

    /**
     * findByPage
     */
    Page<AnimeCategoryVO> findByPage(PageRequestWrapper<AnimeCategoryQuery> query);
}
