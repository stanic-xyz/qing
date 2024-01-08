package cn.chenyunlong.qing.domain.anime.recommend.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import cn.chenyunlong.qing.domain.anime.recommend.dto.creator.RecommendCreator;
import cn.chenyunlong.qing.domain.anime.recommend.dto.query.RecommendQuery;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendCreateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendQueryRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.request.RecommendUpdateRequest;
import cn.chenyunlong.qing.domain.anime.recommend.dto.response.RecommendResponse;
import cn.chenyunlong.qing.domain.anime.recommend.dto.updater.RecommendUpdater;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendDetailVO;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        CustomMapper.class,
        GenericEnumMapper.class,
        DateMapper.class
})
public interface RecommendMapper {
    RecommendMapper INSTANCE = Mappers.getMapper(RecommendMapper.class);

    Recommend dtoToEntity(RecommendCreator dto);

    RecommendUpdater request2Updater(RecommendUpdateRequest request);

    RecommendCreator request2Dto(RecommendCreateRequest request);

    RecommendQuery request2Query(RecommendQueryRequest request);

    RecommendResponse vo2Response(RecommendVO vo);

    RecommendResponse vo2CustomResponse(RecommendVO vo);

    RecommendResponse vo2CustomResponse(RecommendDetailVO vo);
}
