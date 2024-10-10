package cn.chenyunlong.qing.domain.productcenter.template.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCategoryCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateCategoryQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateCategoryUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateCategoryVO;
import org.springframework.data.domain.Page;

public interface ITemplateCategoryService {

    /**
     * create
     */
    Long createTemplateCategory(TemplateCategoryCreator creator);

    /**
     * update
     */
    void updateTemplateCategory(TemplateCategoryUpdater updater);

    void validTemplateCategory(Long id);

    void invalidTemplateCategory(Long id);

    /**
     * findById
     */
    TemplateCategoryVO findById(Long id);

    /**
     * findByPage
     */
    Page<TemplateCategoryVO> findByPage(PageRequestWrapper<TemplateCategoryQuery> query);
}
