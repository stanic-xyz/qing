package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;

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
}
