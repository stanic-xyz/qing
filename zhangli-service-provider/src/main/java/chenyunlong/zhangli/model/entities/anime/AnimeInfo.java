package chenyunlong.zhangli.model.entities.anime;

import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnimeInfo extends BaseEntity<AnimeInfo> {
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
}
