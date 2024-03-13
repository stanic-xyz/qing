package cn.chenyunlong.qing.domain.anime.tag.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.tag.Tag;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.response.TagResponse;
import cn.chenyunlong.qing.domain.anime.tag.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.tag.dto.vo.TagVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class})
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag dtoToEntity(TagCreator dto);

    TagUpdater request2Updater(TagUpdateRequest request);

    TagCreator request2Dto(TagCreateRequest request);

    TagQuery request2Query(TagQueryRequest request);

    TagResponse vo2CustomResponse(TagVO vo);

    TagResponse vo2Response(TagVO vo);
}
