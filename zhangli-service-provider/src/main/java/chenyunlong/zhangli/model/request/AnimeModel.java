package chenyunlong.zhangli.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeModel {
    @NotNull
    private Long url;
    @NotBlank
    private String name;
    @NotBlank
    private String instruction;
    @NotBlank
    private String district;
    @NotBlank
    private String cover;
    private String type;
    private String orignalName;
    private String otherName;
    private String author;
    private String company;
    private String premiereDate;
    private String playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
    private String playHeat;
}
