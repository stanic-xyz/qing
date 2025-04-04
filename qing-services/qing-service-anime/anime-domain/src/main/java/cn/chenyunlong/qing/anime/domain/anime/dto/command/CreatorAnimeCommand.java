package cn.chenyunlong.qing.anime.domain.anime.dto.command;

import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import cn.chenyunlong.qing.anime.domain.anime.enums.District;
import cn.chenyunlong.qing.domain.common.AggregateId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Schema
@Data
public class CreatorAnimeCommand {

    private static final Logger log = LoggerFactory.getLogger(CreatorAnimeCommand.class);
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
    private District district;

    @Schema(
        title = "封面"
    )
    private String cover;

    @Schema(
        title = "aggregateId"
    )
    private AggregateId typeId;

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
    private List<Long> tagIds;

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

    @Schema(hidden = true)
    private String operateUserId;

}
