package chenyunlong.zhangli.model.entities.anime;

import com.stan.zhangli.core.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeInfo extends BaseEntity {
    private Long id;
    private String name;
    private String instruction;
    private String district;
    private String coverUrl;
    private String type;
    private String originalName;
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
