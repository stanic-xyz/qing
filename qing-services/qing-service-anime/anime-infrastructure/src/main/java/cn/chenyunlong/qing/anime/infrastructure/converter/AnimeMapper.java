package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.common.infrustructure.CustomMapper;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.CreatorAnimeCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.response.AnimeResponse;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdateCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.PremiereDate;
import cn.chenyunlong.qing.anime.domain.anime.models.Tags;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.domain.common.converter.AggregateMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {CustomMapper.class, DateMapper.class, AggregateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnimeMapper {

    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    CreatorAnimeCommand requestToCreator(AnimeCreateRequest createRequest);

    Anime creatorToEntity(CreatorAnimeCommand request);

    AnimeUpdateCommand request2Updater(AnimeUpdateRequest request);

    AnimeQuery request2Query(AnimeQueryRequest request);

    AnimeResponse vo2CustomResponse(AnimeVO vo);

    AnimeVO entityToVo(Anime anime);

    AnimeDetailVO entityToDetailVo(Anime anime);

    AnimeResponse vo2Response(AnimeVO vo);

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

    default PremiereDate toPremiereDate(LocalDate value) {
        return new PremiereDate(value);
    }
}
