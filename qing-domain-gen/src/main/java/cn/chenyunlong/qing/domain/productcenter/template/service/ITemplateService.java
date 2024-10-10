package cn.chenyunlong.qing.domain.productcenter.template.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateVO;
import org.springframework.data.domain.Page;

public interface ITemplateService {

    /**
     * create
     */
    Long createTemplate(TemplateCreator creator);

    /**
     * update
     */
    void updateTemplate(TemplateUpdater updater);

    void validTemplate(Long id);

    void invalidTemplate(Long id);

    /**
     * findById
     */
    TemplateVO findById(Long id);

    /**
     * findByPage
     */
    Page<TemplateVO> findByPage(PageRequestWrapper<TemplateQuery> query);
}
