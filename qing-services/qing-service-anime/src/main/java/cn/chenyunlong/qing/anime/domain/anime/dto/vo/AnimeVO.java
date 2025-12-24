package cn.chenyunlong.qing.anime.domain.anime.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.anime.domain.anime.PlayStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
public class AnimeVO extends AbstractBaseJpaVo {

    @Schema(title = "name")
    private String name;

    @Schema(title = "instruction")
    private String instruction;

    @Schema(title = "districtId")
    private Long districtId;

    @Schema(title = "districtName")
    private String districtName;

    @Schema(
        title = "coverUrl"
    )
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
        title = "playStatus"
    )
    private PlayStatus playStatus;

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
