package cn.chenyunlong.qing.domain.anime.episode.mapper;

import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.response.EpisodeResponse;
import cn.chenyunlong.qing.domain.anime.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;
import cn.hutool.core.bean.BeanUtil;

public interface EpisodeMapper {

    EpisodeMapper INSTANCE = new EpisodeMapper() {

    };

    default Episode dtoToEntity(EpisodeCreator dto) {
        return BeanUtil.copyProperties(dto, Episode.class);
    }

    default EpisodeUpdater request2Updater(EpisodeUpdateRequest request) {
        return BeanUtil.copyProperties(request, EpisodeUpdater.class);
    }

    default EpisodeCreator request2Dto(EpisodeCreateRequest request) {
        return BeanUtil.copyProperties(request, EpisodeCreator.class);
    }

    default EpisodeQuery request2Query(EpisodeQueryRequest request) {
        return BeanUtil.copyProperties(request, EpisodeQuery.class);
    }

    default EpisodeResponse vo2CustomResponse(EpisodeVO vo) {
        return vo2Response(vo);
    }

    default EpisodeResponse vo2Response(EpisodeVO vo) {
        return BeanUtil.copyProperties(vo, EpisodeResponse.class);
    }
}
