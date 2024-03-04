package cn.chenyunlong.qing.domain.anime.anime.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.anime.anime.PlayStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class AnimeResponse extends AbstractJpaResponse {

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

    @Schema(
        title = "coverUrl"
    )
    private String coverUrl;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
    private String tags;

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
}
