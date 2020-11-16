package chenyunlong.zhangli.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class ImportAnimeModel {
    private Long url;
    private String name;
    private String instruction;
    private String district;
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
