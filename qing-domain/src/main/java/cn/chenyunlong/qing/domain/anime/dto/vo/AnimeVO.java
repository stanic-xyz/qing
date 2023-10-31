package cn.chenyunlong.qing.domain.anime.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.Anime;
import cn.chenyunlong.qing.domain.anime.PlayStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PROTECTED
)
public class AnimeVO extends AbstractBaseJpaVo {
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

    public AnimeVO(Anime source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setInstruction(source.getInstruction());
        this.setDistrictId(source.getDistrictId());
        this.setDistrictName(source.getDistrictName());
        this.setCoverUrl(source.getCoverUrl());
        this.setTypeId(source.getTypeId());
        this.setTypeName(source.getTypeName());
        this.setOriginalName(source.getOriginalName());
        this.setOtherName(source.getOtherName());
        this.setAuthor(source.getAuthor());
        this.setCompanyId(source.getCompanyId());
        this.setCompanyName(source.getCompanyName());
        this.setPremiereDate(source.getPremiereDate());
        this.setPlayStatus(source.getPlayStatus());
        this.setPlotType(source.getPlotType());
        this.setTags(source.getTags());
        this.setOfficialWebsite(source.getOfficialWebsite());
        this.setPlayHeat(source.getPlayHeat());
        this.setOrderNo(source.getOrderNo());
    }
}
