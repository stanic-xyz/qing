package chenyunlong.zhangli.model.dto.anime;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@EqualsAndHashCode
public class AnimeInfoMinimalDTO implements OutputConverter<AnimeInfoMinimalDTO, AnimeInfo> {

    private Long id;
    private String name;
    private String instruction;
    private String districtName;
    private String coverUrl;
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
    private Boolean isNew = false;
}
