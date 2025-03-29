package cn.chenyunlong.qing.domain.anime.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import cn.chenyunlong.qing.domain.anime.anime.models.Anime;
import cn.chenyunlong.qing.domain.common.AggregateId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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
        title = "aggregateId"
    )
    private AggregateId typeId;

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
        title = "plotTypes"
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

    public void updateAnime(Anime anime) {
        anime.update(getName(), getInstruction());
    }
}
