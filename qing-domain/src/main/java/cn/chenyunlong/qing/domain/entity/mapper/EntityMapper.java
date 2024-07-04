package cn.chenyunlong.qing.domain.entity.mapper;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.dto.query.EntityQuery;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityCreateRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityQueryRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityUpdateRequest;
import cn.chenyunlong.qing.domain.entity.dto.response.EntityResponse;
import cn.chenyunlong.qing.domain.entity.dto.updater.EntityUpdater;
import cn.chenyunlong.qing.domain.entity.dto.vo.EntityVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {
        GenericEnumMapper.class,
        DateMapper.class,
        CustomMapper.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    Entity dtoToEntity(EntityCreator dto);

    EntityUpdater request2Updater(EntityUpdateRequest request);

    EntityCreator request2Dto(EntityCreateRequest request);

    EntityQuery request2Query(EntityQueryRequest request);

    EntityResponse vo2CustomResponse(EntityVO vo);

    EntityResponse vo2Response(EntityVO vo);
}
