package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.domain.episode.EpisodeId;
import cn.chenyunlong.qing.anime.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.anime.domain.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.anime.domain.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.anime.domain.episode.dto.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.anime.domain.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.episode.dto.response.EpisodeResponse;
import cn.chenyunlong.qing.anime.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.anime.domain.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.EpisodeEntity;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpisodeMapper {


    Episode dtoToEntity(EpisodeCreator dto);

    EpisodeUpdater request2Updater(EpisodeUpdateRequest request);

    EpisodeCreator request2Dto(EpisodeCreateRequest request);

    EpisodeQuery request2Query(EpisodeQueryRequest request);

    EpisodeResponse vo2CustomResponse(EpisodeVO vo);

    EpisodeResponse vo2Response(EpisodeVO vo);

    EpisodeVO entityToVo(Episode episode);

    default Long map(EpisodeId tagId) {
        return tagId != null ? tagId.getValue() : null;
    }

    default EpisodeId longToTypeId(Long id) {
        return id != null ? EpisodeId.of(id) : null;
    }

    Episode toDomain(EpisodeEntity episodeEntity);

    EpisodeEntity toEntity(Episode entity);
}
