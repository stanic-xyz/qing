package chenyunlong.zhangli.entities.anime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeInfo implements Serializable {

    private Long id;
    @NotBlank
    private String name;
    private String instruction;
    private String district;
    private String coverUrl;
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
