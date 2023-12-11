package cn.chenyunlong.qing.domain.productcenter.templateitem.mapper;

import cn.chenyunlong.qing.domain.productcenter.templateitem.TemplateItem;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.creator.TemplateItemCreator;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.query.TemplateItemQuery;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.response.TemplateItemResponse;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.updater.TemplateItemUpdater;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.vo.TemplateItemVO;
import cn.hutool.core.bean.BeanUtil;

public interface TemplateItemMapper {
    TemplateItemMapper INSTANCE = new TemplateItemMapper() {
    };

    default TemplateItem dtoToEntity(TemplateItemCreator dto) {
        return BeanUtil.copyProperties(dto, TemplateItem.class);
    }

    default TemplateItemUpdater request2Updater(TemplateItemUpdateRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemUpdater.class);
    }

    default TemplateItemCreator request2Dto(TemplateItemCreateRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemCreator.class);
    }

    default TemplateItemQuery request2Query(TemplateItemQueryRequest request) {
        return BeanUtil.copyProperties(request, TemplateItemQuery.class);
    }

    default TemplateItemResponse vo2Response(TemplateItemVO vo) {
        return BeanUtil.copyProperties(vo, TemplateItemResponse.class);
    }

    default TemplateItemResponse vo2CustomResponse(TemplateItemVO vo) {
        return vo2Response(vo);
    }
}
