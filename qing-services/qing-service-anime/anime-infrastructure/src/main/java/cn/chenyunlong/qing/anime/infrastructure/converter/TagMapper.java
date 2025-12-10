package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.TagCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.models.TagId;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagCreator request2Dto(TagCreateRequest request);

    default Long map(TagId tagId) {
        return tagId != null ? tagId.id() : null;
    }
}
