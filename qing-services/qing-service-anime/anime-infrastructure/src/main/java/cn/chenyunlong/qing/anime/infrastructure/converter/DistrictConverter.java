package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.district.District;
import cn.chenyunlong.qing.anime.domain.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.anime.domain.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.anime.domain.district.dto.request.DistrictCreateRequest;
import cn.chenyunlong.qing.anime.domain.district.dto.request.DistrictQueryRequest;
import cn.chenyunlong.qing.anime.domain.district.dto.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.anime.domain.district.dto.response.DistrictResponse;
import cn.chenyunlong.qing.anime.domain.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.anime.domain.district.dto.vo.DistrictVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    CustomMapper.class,
    DateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DistrictConverter {

    DistrictConverter INSTANCE = Mappers.getMapper(DistrictConverter.class);

    District dtoToEntity(DistrictCreator dto);

    DistrictUpdater request2Updater(DistrictUpdateRequest request);

    DistrictCreator request2Dto(DistrictCreateRequest request);

    DistrictQuery request2Query(DistrictQueryRequest request);

    DistrictResponse vo2CustomResponse(DistrictVO vo);

    DistrictResponse vo2Response(DistrictVO vo);

    DistrictVO entityToVo(District district);
}
