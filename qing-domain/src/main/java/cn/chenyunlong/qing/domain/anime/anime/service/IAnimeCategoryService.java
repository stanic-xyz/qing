package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;
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

    void validAnimeCategory(Long id);

    void invalidAnimeCategory(Long id);

    /**
     * findById
     */
    AnimeCategoryVO findById(Long id);

    /**
     * 分页查询
     */
    Page<AnimeCategoryVO> findByPage(PageRequestWrapper<AnimeCategoryQuery> wrapper);
}
