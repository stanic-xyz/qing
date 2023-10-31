package cn.chenyunlong.qing.domain.entity.mapper;

import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.dto.query.EntityQuery;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityCreateRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityQueryRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityUpdateRequest;
import cn.chenyunlong.qing.domain.entity.dto.response.EntityResponse;
import cn.chenyunlong.qing.domain.entity.dto.updater.EntityUpdater;
import cn.chenyunlong.qing.domain.entity.dto.vo.EntityVO;
import cn.hutool.core.bean.BeanUtil;

public interface EntityMapper {
    EntityMapper INSTANCE = new EntityMapper() {
    };

    default Entity dtoToEntity(EntityCreator dto) {
        return BeanUtil.copyProperties(dto, Entity.class);
    }

    default EntityUpdater request2Updater(EntityUpdateRequest request) {
        return BeanUtil.copyProperties(request, EntityUpdater.class);
    }

    default EntityCreator request2Dto(EntityCreateRequest request) {
        return BeanUtil.copyProperties(request, EntityCreator.class);
    }

    default EntityQuery request2Query(EntityQueryRequest request) {
        return BeanUtil.copyProperties(request, EntityQuery.class);
    }

    default EntityResponse vo2CustomResponse(EntityVO vo) {
        return vo2Response(vo);
    }

    default EntityResponse vo2Response(EntityVO vo) {
        return BeanUtil.copyProperties(vo, EntityResponse.class);
    }
}
