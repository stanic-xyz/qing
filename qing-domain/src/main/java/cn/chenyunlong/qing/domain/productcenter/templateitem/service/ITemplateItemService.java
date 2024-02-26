package cn.chenyunlong.qing.domain.productcenter.templateitem.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.creator.TemplateItemCreator;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.query.TemplateItemQuery;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.updater.TemplateItemUpdater;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.vo.TemplateItemVO;
import org.springframework.data.domain.Page;

public interface ITemplateItemService {

    /**
     * create
     */
    Long createTemplateItem(TemplateItemCreator creator);

    /**
     * update
     */
    void updateTemplateItem(TemplateItemUpdater updater);

    /**
     * valid
     */
    void validTemplateItem(Long id);

    /**
     * invalid
     */
    void invalidTemplateItem(Long id);

    /**
     * findById
     */
    TemplateItemVO findById(Long id);

    /**
     * findByPage
     */
    Page<TemplateItemVO> findByPage(PageRequestWrapper<TemplateItemQuery> query);
}
