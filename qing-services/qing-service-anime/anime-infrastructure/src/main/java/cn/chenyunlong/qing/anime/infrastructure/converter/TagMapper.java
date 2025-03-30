package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.dto.creator.TagCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.query.TagQuery;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.response.TagResponse;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.TagUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.TagVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag dtoToEntity(TagCreator dto);

    TagUpdater request2Updater(TagUpdateRequest request);

    TagCreator request2Dto(TagCreateRequest request);

    TagQuery request2Query(TagQueryRequest request);

    TagResponse vo2CustomResponse(TagVO vo);

    TagResponse vo2Response(TagVO vo);

    TagVO entityToVo(Tag tag);
}
