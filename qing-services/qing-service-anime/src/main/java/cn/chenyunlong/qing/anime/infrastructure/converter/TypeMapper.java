package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.anime.domain.type.Type;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.anime.domain.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.TypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeMapper {
    TypeCreator request2Dto(TypeCreateRequest request);

    Type entityToDomain(TypeEntity typeEntity);

    TypeEntity toEntity(Type entity);

    default TypeId toTypeId(Long value) {
        return TypeId.of(value);
    }
}
