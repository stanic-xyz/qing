package chenyunlong.zhangli.model.entities.anime;

import chenyunlong.zhangli.core.domain.BaseEntity;

import java.time.LocalDate;

/**
 * @author Stan
 */
public class AnimeInfo extends BaseEntity {
    private Long id;
    private String name;
    private String instruction;
    private Integer districtId;
    private String districtName;
    private String coverUrl;
    private Integer typeId;
    private String typeName;
    private String originalName;
    private String otherName;
    private String author;
    private String company;
    private LocalDate premiereDate;
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
    private String playHeat;

    public AnimeInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public String getOtherName() {
        return this.otherName;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCompany() {
        return this.company;
    }

    public LocalDate getPremiereDate() {
        return this.premiereDate;
    }

    public String getPlayStatus() {
        return this.playStatus;
    }

    public String getPlotType() {
        return this.plotType;
    }

    public String getTags() {
        return this.tags;
    }

    public String getOfficialWebsite() {
        return this.officialWebsite;
    }

    public String getPlayHeat() {
        return this.playHeat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = premiereDate;
    }

    public void setPlayStatus(String playStatus) {
        this.playStatus = playStatus;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setOfficialWebsite(String officialWebsite) {
        this.officialWebsite = officialWebsite;
    }

    public void setPlayHeat(String playHeat) {
        this.playHeat = playHeat;
    }

    public String toString() {
        return "AnimeInfo(id=" + this.getId() + ", name=" + this.getName() + ", instruction=" + this.getInstruction() + ", districtId=" + this.getDistrictId() + ", districtName=" + this.getDistrictName() + ", coverUrl=" + this.getCoverUrl() + ", typeId=" + this.getTypeId() + ", typeName=" + this.getTypeName() + ", originalName=" + this.getOriginalName() + ", otherName=" + this.getOtherName() + ", author=" + this.getAuthor() + ", company=" + this.getCompany() + ", premiereDate=" + this.getPremiereDate() + ", playStatus=" + this.getPlayStatus() + ", plotType=" + this.getPlotType() + ", tags=" + this.getTags() + ", officialWebsite=" + this.getOfficialWebsite() + ", playHeat=" + this.getPlayHeat() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AnimeInfo)) return false;
        final AnimeInfo other = (AnimeInfo) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$instruction = this.getInstruction();
        final Object other$instruction = other.getInstruction();
        if (this$instruction == null ? other$instruction != null : !this$instruction.equals(other$instruction))
            return false;
        final Object this$districtId = this.getDistrictId();
        final Object other$districtId = other.getDistrictId();
        if (this$districtId == null ? other$districtId != null : !this$districtId.equals(other$districtId))
            return false;
        final Object this$districtName = this.getDistrictName();
        final Object other$districtName = other.getDistrictName();
        if (this$districtName == null ? other$districtName != null : !this$districtName.equals(other$districtName))
            return false;
        final Object this$coverUrl = this.getCoverUrl();
        final Object other$coverUrl = other.getCoverUrl();
        if (this$coverUrl == null ? other$coverUrl != null : !this$coverUrl.equals(other$coverUrl)) return false;
        final Object this$typeId = this.getTypeId();
        final Object other$typeId = other.getTypeId();
        if (this$typeId == null ? other$typeId != null : !this$typeId.equals(other$typeId)) return false;
        final Object this$typeName = this.getTypeName();
        final Object other$typeName = other.getTypeName();
        if (this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName)) return false;
        final Object this$originalName = this.getOriginalName();
        final Object other$originalName = other.getOriginalName();
        if (this$originalName == null ? other$originalName != null : !this$originalName.equals(other$originalName))
            return false;
        final Object this$otherName = this.getOtherName();
        final Object other$otherName = other.getOtherName();
        if (this$otherName == null ? other$otherName != null : !this$otherName.equals(other$otherName)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$company = this.getCompany();
        final Object other$company = other.getCompany();
        if (this$company == null ? other$company != null : !this$company.equals(other$company)) return false;
        final Object this$premiereDate = this.getPremiereDate();
        final Object other$premiereDate = other.getPremiereDate();
        if (this$premiereDate == null ? other$premiereDate != null : !this$premiereDate.equals(other$premiereDate))
            return false;
        final Object this$playStatus = this.getPlayStatus();
        final Object other$playStatus = other.getPlayStatus();
        if (this$playStatus == null ? other$playStatus != null : !this$playStatus.equals(other$playStatus))
            return false;
        final Object this$plotType = this.getPlotType();
        final Object other$plotType = other.getPlotType();
        if (this$plotType == null ? other$plotType != null : !this$plotType.equals(other$plotType)) return false;
        final Object this$tags = this.getTags();
        final Object other$tags = other.getTags();
        if (this$tags == null ? other$tags != null : !this$tags.equals(other$tags)) return false;
        final Object this$officialWebsite = this.getOfficialWebsite();
        final Object other$officialWebsite = other.getOfficialWebsite();
        if (this$officialWebsite == null ? other$officialWebsite != null : !this$officialWebsite.equals(other$officialWebsite))
            return false;
        final Object this$playHeat = this.getPlayHeat();
        final Object other$playHeat = other.getPlayHeat();
        if (this$playHeat == null ? other$playHeat != null : !this$playHeat.equals(other$playHeat)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AnimeInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $instruction = this.getInstruction();
        result = result * PRIME + ($instruction == null ? 43 : $instruction.hashCode());
        final Object $districtId = this.getDistrictId();
        result = result * PRIME + ($districtId == null ? 43 : $districtId.hashCode());
        final Object $districtName = this.getDistrictName();
        result = result * PRIME + ($districtName == null ? 43 : $districtName.hashCode());
        final Object $coverUrl = this.getCoverUrl();
        result = result * PRIME + ($coverUrl == null ? 43 : $coverUrl.hashCode());
        final Object $typeId = this.getTypeId();
        result = result * PRIME + ($typeId == null ? 43 : $typeId.hashCode());
        final Object $typeName = this.getTypeName();
        result = result * PRIME + ($typeName == null ? 43 : $typeName.hashCode());
        final Object $originalName = this.getOriginalName();
        result = result * PRIME + ($originalName == null ? 43 : $originalName.hashCode());
        final Object $otherName = this.getOtherName();
        result = result * PRIME + ($otherName == null ? 43 : $otherName.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        final Object $company = this.getCompany();
        result = result * PRIME + ($company == null ? 43 : $company.hashCode());
        final Object $premiereDate = this.getPremiereDate();
        result = result * PRIME + ($premiereDate == null ? 43 : $premiereDate.hashCode());
        final Object $playStatus = this.getPlayStatus();
        result = result * PRIME + ($playStatus == null ? 43 : $playStatus.hashCode());
        final Object $plotType = this.getPlotType();
        result = result * PRIME + ($plotType == null ? 43 : $plotType.hashCode());
        final Object $tags = this.getTags();
        result = result * PRIME + ($tags == null ? 43 : $tags.hashCode());
        final Object $officialWebsite = this.getOfficialWebsite();
        result = result * PRIME + ($officialWebsite == null ? 43 : $officialWebsite.hashCode());
        final Object $playHeat = this.getPlayHeat();
        result = result * PRIME + ($playHeat == null ? 43 : $playHeat.hashCode());
        return result;
    }
}
