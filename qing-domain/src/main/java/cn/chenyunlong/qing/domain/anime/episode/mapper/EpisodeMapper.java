package cn.chenyunlong.qing.domain.anime.episode.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.response.EpisodeResponse;
import cn.chenyunlong.qing.domain.anime.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpisodeMapper {

    EpisodeMapper INSTANCE = Mappers.getMapper(EpisodeMapper.class);

    Episode dtoToEntity(EpisodeCreator dto);

    EpisodeUpdater request2Updater(EpisodeUpdateRequest request);

    EpisodeCreator request2Dto(EpisodeCreateRequest request);

    EpisodeQuery request2Query(EpisodeQueryRequest request);

    EpisodeResponse vo2CustomResponse(EpisodeVO vo);

    EpisodeResponse vo2Response(EpisodeVO vo);

    EpisodeVO entityToVo(Episode episode);
}
