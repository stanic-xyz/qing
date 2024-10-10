package cn.chenyunlong.qing.domain.attribute.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.attribute.dto.creator.AttributeCreator;
import cn.chenyunlong.qing.domain.attribute.dto.query.AttributeQuery;
import cn.chenyunlong.qing.domain.attribute.dto.updater.AttributeUpdater;
import cn.chenyunlong.qing.domain.attribute.dto.vo.AttributeVO;
import org.springframework.data.domain.Page;

public interface IAttributeService {

    /**
     * create
     */
    Long createAttribute(AttributeCreator creator);

    /**
     * update
     */
    void updateAttribute(AttributeUpdater updater);

    void validAttribute(Long id);

    void invalidAttribute(Long id);

    /**
     * findById
     */
    AttributeVO findById(Long id);

    /**
     * findByPage
     */
    Page<AttributeVO> findByPage(PageRequestWrapper<AttributeQuery> query);
}
