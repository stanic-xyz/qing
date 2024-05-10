package cn.chenyunlong.qing.domain.productcenter.template.mapper;

import cn.chenyunlong.qing.domain.productcenter.template.TemplateItemRel;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateItemRelCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateItemRelQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateItemRelCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateItemRelQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateItemRelUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateItemRelResponse;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateItemRelUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateItemRelVO;
import cn.hutool.core.bean.BeanUtil;

public interface TemplateItemRelMapper {

    TemplateItemRelMapper INSTANCE = new TemplateItemRelMapper() {

    };

    default TemplateItemRel dtoToEntity(TemplateItemRelCreator dto) {
        return BeanUtil.copyProperties(dto, TemplateItemRel.class);
    }

    default TemplateItemRelUpdater request2Updater(TemplateItemRelUpdateRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemRelUpdater.class);
    }

    default TemplateItemRelCreator request2Dto(TemplateItemRelCreateRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemRelCreator.class);
    }

    default TemplateItemRelQuery request2Query(TemplateItemRelQueryRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemRelQuery.class);
    }

    default TemplateItemRelResponse vo2Response(TemplateItemRelVO vo) {
        return BeanUtil.copyProperties(vo, TemplateItemRelResponse.class);
    }

    default TemplateItemRelResponse vo2CustomResponse(TemplateItemRelVO vo) {
        return vo2Response(vo);
    }
}
