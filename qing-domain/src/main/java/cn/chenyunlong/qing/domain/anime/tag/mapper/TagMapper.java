package cn.chenyunlong.qing.domain.anime.tag.mapper;

import cn.chenyunlong.qing.domain.anime.tag.Tag;
import cn.chenyunlong.qing.domain.anime.tag.dto.creator.TagCreator;
import cn.chenyunlong.qing.domain.anime.tag.dto.query.TagQuery;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.domain.anime.tag.dto.response.TagResponse;
import cn.chenyunlong.qing.domain.anime.tag.dto.updater.TagUpdater;
import cn.chenyunlong.qing.domain.anime.tag.dto.vo.TagVO;
import cn.hutool.core.bean.BeanUtil;

public interface TagMapper {
    TagMapper INSTANCE = new TagMapper() {
    };

    default Tag dtoToEntity(TagCreator dto) {
        return BeanUtil.copyProperties(dto, Tag.class);
    }

    default TagUpdater request2Updater(TagUpdateRequest request) {
        return BeanUtil.copyProperties(request, TagUpdater.class);
    }

    default TagCreator request2Dto(TagCreateRequest request) {
        return BeanUtil.copyProperties(request, TagCreator.class);
    }

    default TagQuery request2Query(TagQueryRequest request) {
        return BeanUtil.copyProperties(request, TagQuery.class);
    }

    default TagResponse vo2CustomResponse(TagVO vo) {
        return vo2Response(vo);
    }

    default TagResponse vo2Response(TagVO vo) {
        return BeanUtil.copyProperties(vo, TagResponse.class);
    }
}
