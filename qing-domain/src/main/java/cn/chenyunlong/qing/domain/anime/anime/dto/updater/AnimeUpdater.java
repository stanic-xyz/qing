package cn.chenyunlong.qing.domain.anime.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.anime.district.District;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Schema
@Data
public class AnimeUpdater {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    @Schema(
        title = "districtId"
    )
    private Long districtId;

    @Schema(
        title = "districtName"
    )
    private String districtName;

    @Schema(title = "封面的附件Id")
    private String cover;

    @Schema(
        title = "typeId"
    )
    private Long typeId;

    @Schema(
        title = "typeName"
    )
    private String typeName;

    @Schema(
        title = "originalName"
    )
    private String originalName;

    @Schema(
        title = "otherName"
    )
    private String otherName;

    @Schema(
        title = "author"
    )
    private String author;

    @Schema(
        title = "companyId"
    )
    private Long companyId;

    @Schema(
        title = "companyName"
    )
    private String companyName;

    @Schema(
        title = "premiereDate"
    )
    private LocalDate premiereDate;

    @Schema(
        title = "playStatus"
    )
    private PlayStatus playStatus;

    @Schema(
        title = "plotType"
    )
    private String plotType;

    @Schema(
        title = "tags"
    )
    private List<Long> tags;

    @Schema(
        title = "officialWebsite"
    )
    private String officialWebsite;

    @Schema(
        title = "playHeat"
    )
    private String playHeat;

    @Schema(
        title = "orderNo"
    )
    private Integer orderNo;

    private Long id;

    public void updateAnime(Anime anime, List<Tag> tagList, District district, AnimeCategory animeCategory) {
        Optional.ofNullable(getName()).ifPresent(anime::setName);
        Optional.ofNullable(getInstruction()).ifPresent(anime::setInstruction);
        Optional.ofNullable(getDistrictId()).ifPresent(anime::setDistrictId);
        Optional.ofNullable(getDistrictName()).ifPresent(anime::setDistrictName);
        Optional.ofNullable(getCover()).ifPresent(anime::setCover);
        Optional.ofNullable(district).ifPresent(district1 -> {
            anime.setDistrictId(district1.getId());
            anime.setDistrictName(district1.getName());
        });
        Optional.ofNullable(animeCategory).ifPresent(district1 -> {
            anime.setTypeId(district1.getId());
            anime.setTypeName(district1.getName());
        });
        Optional.ofNullable(getTypeId()).ifPresent(anime::setTypeId);
        Optional.ofNullable(getTypeName()).ifPresent(anime::setTypeName);
        Optional.ofNullable(getOriginalName()).ifPresent(anime::setOriginalName);
        Optional.ofNullable(getOtherName()).ifPresent(anime::setOtherName);
        Optional.ofNullable(getAuthor()).ifPresent(anime::setAuthor);
        Optional.ofNullable(getCompanyId()).ifPresent(anime::setCompanyId);
        Optional.ofNullable(getCompanyName()).ifPresent(anime::setCompanyName);
        Optional.ofNullable(getPremiereDate()).ifPresent(anime::setPremiereDate);
        Optional.ofNullable(getPlayStatus()).ifPresent(anime::setPlayStatus);
        Optional.ofNullable(getPlotType()).ifPresent(anime::setPlotType);
        Optional.ofNullable(tagList).ifPresent(tagLis -> anime.setTags(tagLis.stream().map(Tag::getName).collect(Collectors.joining(","))));
        Optional.ofNullable(getOfficialWebsite()).ifPresent(anime::setOfficialWebsite);
        Optional.ofNullable(getPlayHeat()).ifPresent(anime::setPlayHeat);
        Optional.ofNullable(getOrderNo()).ifPresent(anime::setOrderNo);
    }
}
