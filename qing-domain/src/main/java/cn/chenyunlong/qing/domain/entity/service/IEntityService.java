package cn.chenyunlong.qing.domain.entity.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.dto.query.EntityQuery;
import cn.chenyunlong.qing.domain.entity.dto.updater.EntityUpdater;
import cn.chenyunlong.qing.domain.entity.dto.vo.EntityVO;
import org.springframework.data.domain.Page;

public interface IEntityService {

    /**
     * create
     */
    Long createEntity(EntityCreator creator);

    /**
     * update
     */
    void updateEntity(EntityUpdater updater);

    void validEntity(Long id);

    void invalidEntity(Long id);

    /**
     * findById
     */
    EntityVO findById(Long id);

    /**
     * findByPage
     */
    Page<EntityVO> findByPage(PageRequestWrapper<EntityQuery> query);

    void updateZanCount(Long entityId);
}
