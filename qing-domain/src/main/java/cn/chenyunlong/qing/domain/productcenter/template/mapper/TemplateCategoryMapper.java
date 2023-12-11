package cn.chenyunlong.qing.domain.productcenter.template.mapper;

import cn.chenyunlong.qing.domain.productcenter.template.TemplateCategory;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCategoryCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateCategoryQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateCategoryResponse;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateCategoryUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateCategoryVO;
import cn.hutool.core.bean.BeanUtil;

public interface TemplateCategoryMapper {
    TemplateCategoryMapper INSTANCE = new TemplateCategoryMapper() {
    };

    default TemplateCategory dtoToEntity(TemplateCategoryCreator dto) {
        return BeanUtil.copyProperties(dto, TemplateCategory.class);
    }

    default TemplateCategoryUpdater request2Updater(TemplateCategoryUpdateRequest request) {
        return BeanUtil.copyProperties(request, TemplateCategoryUpdater.class);
    }

    default TemplateCategoryCreator request2Dto(TemplateCategoryCreateRequest request) {
        return BeanUtil.copyProperties(request, TemplateCategoryCreator.class);
    }

    default TemplateCategoryQuery request2Query(TemplateCategoryQueryRequest request) {
        return BeanUtil.copyProperties(request, TemplateCategoryQuery.class);
    }

    default TemplateCategoryResponse vo2Response(TemplateCategoryVO vo) {
        return BeanUtil.copyProperties(vo, TemplateCategoryResponse.class);
    }

    default TemplateCategoryResponse vo2CustomResponse(TemplateCategoryVO vo) {
        return vo2Response(vo);
    }
}
