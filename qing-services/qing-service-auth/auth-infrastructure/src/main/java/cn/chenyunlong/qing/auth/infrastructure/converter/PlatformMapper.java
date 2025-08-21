package cn.chenyunlong.qing.auth.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.auth.domain.platform.Platform;
import cn.chenyunlong.qing.auth.domain.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.auth.domain.platform.dto.query.PlatformQuery;
import cn.chenyunlong.qing.auth.domain.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.auth.domain.platform.dto.request.PlatformQueryRequest;
import cn.chenyunlong.qing.auth.domain.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.auth.domain.platform.dto.response.PlatformResponse;
import cn.chenyunlong.qing.auth.domain.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.auth.domain.platform.dto.vo.PlatformVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    CustomMapper.class,
    DateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlatformMapper {

    PlatformMapper INSTANCE = Mappers.getMapper(PlatformMapper.class);

    Platform dtoToEntity(PlatformCreator dto);

    PlatformUpdater request2Updater(PlatformUpdateRequest request);

    PlatformCreator request2Dto(PlatformCreateRequest request);

    PlatformQuery request2Query(PlatformQueryRequest request);

    PlatformResponse vo2Response(PlatformVO vo);

    PlatformResponse vo2CustomResponse(PlatformVO vo);
}
