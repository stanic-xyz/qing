package chenyunlong.zhangli.model.dto.anime;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class AnimeInfoMinimalDTO implements OutputConverter<AnimeInfoMinimalDTO, AnimeInfo> {

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
