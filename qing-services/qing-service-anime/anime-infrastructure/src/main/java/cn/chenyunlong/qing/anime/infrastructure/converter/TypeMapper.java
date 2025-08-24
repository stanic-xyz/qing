package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.anime.domain.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.anime.domain.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.anime.domain.type.dto.request.TypeQueryRequest;
import cn.chenyunlong.qing.anime.domain.type.dto.request.TypeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.type.dto.response.TypeResponse;
import cn.chenyunlong.qing.anime.domain.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.anime.domain.type.dto.vo.TypeVO;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.TypeEntity;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeMapper {

    Type dtoToEntity(TypeCreator dto);

    TypeUpdater request2Updater(TypeUpdateRequest request);

    TypeCreator request2Dto(TypeCreateRequest request);

    TypeQuery request2Query(TypeQueryRequest request);

    TypeResponse vo2CustomResponse(TypeVO vo);

    TypeResponse vo2Response(TypeVO vo);

    TypeVO entityToVo(Type type);

    Type entityToDomain(TypeEntity typeEntity);

    TypeEntity toEntity(Type entity);

    /**
     * TypeId转Long
     */
    default Long typeIdToLong(TypeId typeId) {
        return typeId != null ? typeId.getValue() : null;
    }

    /**
     * Long转TypeId
     */
    default TypeId longToTypeId(Long id) {
        return id != null ? TypeId.of(id) : null;
    }
}
