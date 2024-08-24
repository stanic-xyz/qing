package cn.chenyunlong.qing.domain.anime.playlist.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import cn.chenyunlong.qing.domain.anime.playlist.dto.creator.PlayListCreator;
import cn.chenyunlong.qing.domain.anime.playlist.dto.query.PlayListQuery;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListCreateRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListQueryRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.request.PlayListUpdateRequest;
import cn.chenyunlong.qing.domain.anime.playlist.dto.response.PlayListResponse;
import cn.chenyunlong.qing.domain.anime.playlist.dto.updater.PlayListUpdater;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
    CustomMapper.class,
    DateMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayListMapper {

    PlayListMapper INSTANCE = Mappers.getMapper(PlayListMapper.class);

    PlayList dtoToEntity(PlayListCreator dto);

    PlayListUpdater request2Updater(PlayListUpdateRequest request);

    PlayListCreator request2Dto(PlayListCreateRequest request);

    PlayListQuery request2Query(PlayListQueryRequest request);

    PlayListResponse vo2Response(PlayListVO vo);

    PlayListResponse vo2CustomResponse(PlayListVO vo);

    PlayListVO entityToVo(PlayList playList);
}
