package cn.chenyunlong.qing.domain.type.mapper;

import cn.chenyunlong.qing.domain.type.Type;
import cn.chenyunlong.qing.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.domain.type.dto.request.TypeQueryRequest;
import cn.chenyunlong.qing.domain.type.dto.request.TypeUpdateRequest;
import cn.chenyunlong.qing.domain.type.dto.response.TypeResponse;
import cn.chenyunlong.qing.domain.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.type.dto.vo.TypeVO;
import cn.hutool.core.bean.BeanUtil;

public interface TypeMapper {

    TypeMapper INSTANCE = new TypeMapper() {

    };

    default Type dtoToEntity(TypeCreator dto) {
        return BeanUtil.copyProperties(dto, Type.class);
    }

    default TypeUpdater request2Updater(TypeUpdateRequest request) {
        return BeanUtil.copyProperties(request, TypeUpdater.class);
    }

    default TypeCreator request2Dto(TypeCreateRequest request) {
        return BeanUtil.copyProperties(request, TypeCreator.class);
    }

    default TypeQuery request2Query(TypeQueryRequest request) {
        return BeanUtil.copyProperties(request, TypeQuery.class);
    }

    default TypeResponse vo2CustomResponse(TypeVO vo) {
        return vo2Response(vo);
    }

    default TypeResponse vo2Response(TypeVO vo) {
        return BeanUtil.copyProperties(vo, TypeResponse.class);
    }
}
