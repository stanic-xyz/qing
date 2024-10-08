package cn.chenyunlong.qing.domain.anime.type.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.anime.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.anime.type.dto.vo.TypeVO;
import org.springframework.data.domain.Page;

public interface ITypeService {

    /**
     * create
     */
    Long createType(TypeCreator creator);

    /**
     * update
     */
    void updateType(TypeUpdater updater);

    void validType(Long id);

    void invalidType(Long id);

    /**
     * findById
     */
    TypeVO findById(Long id);

    /**
     * findByPage
     */
    Page<TypeVO> findByPage(PageRequestWrapper<TypeQuery> query);
}
