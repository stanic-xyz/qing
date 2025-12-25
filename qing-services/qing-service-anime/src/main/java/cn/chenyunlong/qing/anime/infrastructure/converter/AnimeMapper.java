package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.CreatorAnimeCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.anime.domain.anime.models.*;
import cn.chenyunlong.qing.anime.domain.type.TypeId;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.AggregateMapper;
import cn.chenyunlong.qing.anime.infrastructure.converter.base.DateMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomMapper.class, DateMapper.class,
        AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnimeMapper {

    @Named("creatorToAnime")
    default Anime creatorToAnime(CreatorAnimeCommand request) {
        if (request == null) {
            return null;
        }
        return Anime.create(
                AnimeId.of(IdUtil.getSnowflakeNextId()),
                request.getName(),
                new AnimeCategory(request.getCategoryId(), request.getName()),
                request.getInstruction());
    }

    AnimeVO entityToVo(Anime anime);

    default List<Long> map(String value) {
        if (StrUtil.isBlank(value)) {
            return CollUtil.newArrayList();
        }
        return StrUtil.split(value, ",").stream().map(Long::parseLong).collect(Collectors.toList());
    }

    AnimeEntity domainToEntity(Anime anime);

    default LocalDate primerDateMap(PremiereDate value) {
        return value != null ? value.date() : null;
    }

    default String mapTags(Tags value) {
        return value != null ? value.toString() : null;
    }

    default Tags toTags(String value) {
        return Tags.create(StrUtil.split(value, ","));
    }

    Anime entityToDomain(AnimeEntity animeEntity);

    @Named("entityToAnime")
    default Anime entityToAnime(AnimeEntity entity) {
        if (entity == null) {
            return null;
        }
        return Anime.rebuild(
                AnimeId.of(entity.getId()),
                entity.getName(),
                null, entity.getInstruction(),
                null, entity.getCover(),
                null, entity.getOriginalName(),
                entity.getOtherName(),
                entity.getAuthor(),
                null, null, entity.getPlayStatus(),
                null, null, entity.getOfficialWebsite(),
                entity.getPlayHeat(),
                entity.getLastUpdateTime(),
                entity.getOrderNo(),
                entity.getIsOnShelf(), null);
    }

    default PremiereDate toPremiereDate(LocalDate value) {
        return new PremiereDate(value);
    }

    default List<String> toTagsList(String value) {
        if (StrUtil.isBlank(value)) {
            return CollUtil.newArrayList();
        }
        return StrUtil.split(value, ",").stream().map(String::trim).collect(Collectors.toList());
    }

    default Long toAnimeIdValue(AnimeId tagId) {
        return tagId != null ? tagId.id() : null;
    }

    default AnimeId longToAnimeId(Long id) {
        return id != null ? AnimeId.of(id) : null;
    }

    default Long toValue(TypeId tagId) {
        return tagId != null ? tagId.id() : null;
    }

    default TypeId longToTypeId(Long id) {
        return id != null ? TypeId.of(id) : null;
    }

}
