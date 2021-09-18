package chenyunlong.zhangli.model.dto.anime;

import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


/**
 * @author Stan
 */
@Data
public class AnimeInfoDetailDTO implements OutputConverter<AnimeInfoDetailDTO, AnimeInfo> {
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
    private List<PlayListDTO> playList;

}
