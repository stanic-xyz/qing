package cn.chenyunlong.qing.domain.productcenter.template.mapper;

import cn.chenyunlong.qing.domain.productcenter.template.Template;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateResponse;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateVO;
import cn.hutool.core.bean.BeanUtil;

public interface TemplateMapper {
    TemplateMapper INSTANCE = new TemplateMapper() {
    };

    default Template dtoToEntity(TemplateCreator dto) {
        return BeanUtil.copyProperties(dto, Template.class);
    }

    default TemplateUpdater request2Updater(TemplateUpdateRequest request) {
        return BeanUtil.copyProperties(request, TemplateUpdater.class);
    }

    default TemplateCreator request2Dto(TemplateCreateRequest request) {
        return BeanUtil.copyProperties(request, TemplateCreator.class);
    }

    default TemplateQuery request2Query(TemplateQueryRequest request) {
        return BeanUtil.copyProperties(request, TemplateQuery.class);
    }

    default TemplateResponse vo2Response(TemplateVO vo) {
        return BeanUtil.copyProperties(vo, TemplateResponse.class);
    }

    default TemplateResponse vo2CustomResponse(TemplateVO vo) {
        return vo2Response(vo);
    }
}
