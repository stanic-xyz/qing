package cn.chenyunlong.qing.anime.application.mapper;

import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.domain.anime.models.Tags;
import cn.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeApplicationMapper {
    AnimeDTO mapToDto(Anime anime);

    default Long map(AnimeId tagId) {
        return tagId != null ? tagId.id() : null;
    }

    default AnimeId longToAnimeId(Long id) {
        return id != null ? AnimeId.of(id) : null;
    }

    default List<String> mapTagValues(Tags value) {
        return value.tags();
    }

    default Tags mapToTags(List<String> tagNames) {
        if (CollUtil.isNotEmpty(tagNames)) {
            return Tags.create(tagNames);
        }
        return Tags.createEmptyTags();
    }
}
