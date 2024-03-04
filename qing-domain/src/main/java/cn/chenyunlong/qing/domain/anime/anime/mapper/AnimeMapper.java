package cn.chenyunlong.qing.domain.anime.anime.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class})
public interface AnimeMapper {

    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    Anime dtoToEntity(AnimeCreator dto);

    AnimeUpdater request2Updater(AnimeUpdateRequest request);

    AnimeCreator request2Dto(AnimeCreateRequest request);

    AnimeQuery request2Query(AnimeQueryRequest request);

    AnimeResponse vo2CustomResponse(AnimeVO vo);

    AnimeVO entityToVo(Anime dto);

    AnimeResponse vo2Response(AnimeVO vo);
}
