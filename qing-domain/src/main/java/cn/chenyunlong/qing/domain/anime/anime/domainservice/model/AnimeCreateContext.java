package cn.chenyunlong.qing.domain.anime.anime.domainservice.model;

import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.district.District;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema
@Data
@AllArgsConstructor
public class AnimeCreateContext {

    @NotNull
    private AnimeCreator animeCreator;

    @NotNull
    private List<Tag> tagList;

    @NotNull
    private District district;

    @NotNull
    private AnimeCategory animeCategory;

    public static AnimeCreateContext createContext(AnimeCreateRequest createRequest, List<Tag> tagList, District district, AnimeCategory animeCategory) {
        AnimeCreator requestToCreator = AnimeMapper.INSTANCE.requestToCreator(createRequest);
        return new AnimeCreateContext(requestToCreator, tagList, district, animeCategory);
    }
}
