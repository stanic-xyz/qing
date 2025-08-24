package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 动漫基础设施层映射器
 * 负责领域对象和JPA实体之间的转换
 *
 * @author 陈云龙
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AggregateMapper.class)
public interface AnimeInfrastructureMapper {

    /**
     * 领域对象转换为JPA实体
     *
     * @param anime 动漫领域对象
     * @return JPA实体
     */
    AnimeEntity toJpaEntity(Anime anime);

    /**
     * JPA实体转换为领域对象
     *
     * @param entity JPA实体
     * @return 动漫领域对象
     */
    Anime toDomain(AnimeEntity entity);

    @Named("entityToDomain")
    default Anime entityToDomain(AnimeEntity entity) {
        if (entity == null) {
            return null;
        }
        return Anime.rebuild(
                AnimeId.of(entity.getId()),
                entity.getName(), new AnimeCategory(CategoryId.of(entity.getTypeId()), ""),
                entity.getInstruction(), District.CHINA,
                entity.getCover(), new AnimeType(TypeId.of(entity.getTypeId()), entity.getTypeName()),
                entity.getOriginalName(),
                entity.getOtherName(),
                entity.getAuthor(),
                new Company(entity.getCompanyId(), entity.getCompanyName()),
                entity.getPremiereDate() != null ? new PremiereDate(entity.getPremiereDate()) : null,
                entity.getPlayStatus(),
                new PlotTypes(CollUtil.toList()),
                entity.getTags() != null ? stringToTags(entity.getTags()) : Tags.createEmptyTags(),
                entity.getOfficialWebsite(),
                entity.getPlayHeat(),
                entity.getLastUpdateTime(),
                entity.getOrderNo(),
                entity.getIsOnShelf(),
                entity.getIsDeleted());
    }

    default PlotTypes toPlotType(String plotType) {
        if (StrUtil.isNotBlank(plotType)) {
            new PlotTypes(StrUtil.split(plotType, ","));
        }
        return new PlotTypes(CollUtil.toList());
    }

    // 自定义映射方法

    default AnimeId longToAnimeId(Long id) {
        return id != null ? AnimeId.of(id) : null;
    }

    @org.mapstruct.Named("stringToTags")
    default Tags stringToTags(String tags) {
        if (StrUtil.isBlank(tags)) {
            return Tags.createEmptyTags();
        }
        List<String> tagList = Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
        return Tags.create(tagList);
    }

    // 反向映射方法

    default LocalDate map(PremiereDate premiereDate) {
        return premiereDate != null ? premiereDate.date() : null;
    }

    default Long map(AnimeId animeId) {
        return animeId != null ? animeId.getValue() : null;
    }

    default PremiereDate map(LocalDate localDate) {
        return localDate != null ? new PremiereDate(localDate) : null;
    }

    default List<String> map(String tags) {
        if (StrUtil.isBlank(tags)) {
            return CollUtil.toList();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }

    default String playStatusToString(PlayStatus playStatus) {
        return playStatus != null ? playStatus.name() : null;
    }

    default String tagsToString(Tags tags) {
        return tags != null ? tags.toString() : null;
    }

    /**
     * 将领域对象转换为实体对象
     *
     * @param anime 领域对象
     * @return 实体对象
     */
    AnimeEntity toEntity(Anime anime);

}
