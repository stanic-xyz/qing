package cn.chenyunlong.qing.domain.anime.anime.mapper;

import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateCommand;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.infrustructure.converter.CustomMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CustomMapper.class, DateMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnimeMapper {

    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    AnimeCreator requestToCreator(AnimeCreateCommand createRequest);

    Anime creatorToEntity(AnimeCreator request);

    AnimeUpdater request2Updater(AnimeUpdateRequest request);

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
}
