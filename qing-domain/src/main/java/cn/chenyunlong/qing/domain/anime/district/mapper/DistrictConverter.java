package cn.chenyunlong.qing.domain.anime.district.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.district.District;
import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictCreateRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictQueryRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.response.DistrictResponse;
import cn.chenyunlong.qing.domain.anime.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.anime.district.dto.vo.DistrictVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
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
}
