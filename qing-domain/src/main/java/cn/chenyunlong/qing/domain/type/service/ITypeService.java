package cn.chenyunlong.qing.domain.type.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.type.dto.vo.TypeVO;
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

    /**
     * valid
     */
    void validType(Long id);

    /**
     * invalid
     */
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
