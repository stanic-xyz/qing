package cn.chenyunlong.qing.domain.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.Anime;
import cn.chenyunlong.qing.domain.anime.PlayStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.util.Optional;
import lombok.Data;

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

    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = premiereDate;
    }

    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(PlayStatus playStatus) {
        this.playStatus = playStatus;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getOfficialWebsite() {
        return officialWebsite;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public String getPlayHeat() {
        return playHeat;
    }

    public void setPlayHeat(String playHeat) {
        this.playHeat = playHeat;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public void updateAnime(Anime param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getInstruction()).ifPresent(param::setInstruction);
        Optional.ofNullable(getDistrictId()).ifPresent(param::setDistrictId);
        Optional.ofNullable(getDistrictName()).ifPresent(param::setDistrictName);
        Optional.ofNullable(getCoverUrl()).ifPresent(param::setCoverUrl);
        Optional.ofNullable(getTypeId()).ifPresent(param::setTypeId);
        Optional.ofNullable(getTypeName()).ifPresent(param::setTypeName);
        Optional.ofNullable(getOriginalName()).ifPresent(param::setOriginalName);
        Optional.ofNullable(getOtherName()).ifPresent(param::setOtherName);
        Optional.ofNullable(getAuthor()).ifPresent(param::setAuthor);
        Optional.ofNullable(getCompanyId()).ifPresent(param::setCompanyId);
        Optional.ofNullable(getCompanyName()).ifPresent(param::setCompanyName);
        Optional.ofNullable(getPremiereDate()).ifPresent(param::setPremiereDate);
        Optional.ofNullable(getPlayStatus()).ifPresent(param::setPlayStatus);
        Optional.ofNullable(getPlotType()).ifPresent(param::setPlotType);
        Optional.ofNullable(getTags()).ifPresent(param::setTags);
        Optional.ofNullable(getOfficialWebsite()).ifPresent(param::setOfficialWebsite);
        Optional.ofNullable(getPlayHeat()).ifPresent(param::setPlayHeat);
        Optional.ofNullable(getOrderNo()).ifPresent(param::setOrderNo);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
