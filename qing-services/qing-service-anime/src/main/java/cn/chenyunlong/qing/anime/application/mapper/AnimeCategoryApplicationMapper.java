package cn.chenyunlong.qing.anime.application.mapper;

import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeCategoryApplicationMapper {

    AnimeCategory toAnimeCategory(Category category);
}
