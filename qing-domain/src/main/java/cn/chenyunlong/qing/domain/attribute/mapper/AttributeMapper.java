package cn.chenyunlong.qing.domain.attribute.mapper;

import cn.chenyunlong.qing.domain.attribute.Attribute;
import cn.chenyunlong.qing.domain.attribute.dto.creator.AttributeCreator;
import cn.chenyunlong.qing.domain.attribute.dto.query.AttributeQuery;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeCreateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeQueryRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeUpdateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.response.AttributeResponse;
import cn.chenyunlong.qing.domain.attribute.dto.updater.AttributeUpdater;
import cn.chenyunlong.qing.domain.attribute.dto.vo.AttributeVO;
import cn.hutool.core.bean.BeanUtil;

public interface AttributeMapper {
    AttributeMapper INSTANCE = new AttributeMapper() {
    };

    default Attribute dtoToEntity(AttributeCreator dto) {
        return BeanUtil.copyProperties(dto, Attribute.class);
    }

    default AttributeUpdater request2Updater(AttributeUpdateRequest request) {
        return BeanUtil.copyProperties(request, AttributeUpdater.class);
    }

    default AttributeCreator request2Dto(AttributeCreateRequest request) {
        return BeanUtil.copyProperties(request, AttributeCreator.class);
    }

    default AttributeQuery request2Query(AttributeQueryRequest request) {
        return BeanUtil.copyProperties(request, AttributeQuery.class);
    }

    default AttributeResponse vo2Response(AttributeVO vo) {
        return BeanUtil.copyProperties(vo, AttributeResponse.class);
    }

    default AttributeResponse vo2CustomResponse(AttributeVO vo) {
        return vo2Response(vo);
    }
}
