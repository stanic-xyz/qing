package chenyunlong.zhangli.model.vo.anime;

import chenyunlong.zhangli.entities.anime.AnimeEpisode;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class AnimeInfoVo {
    private Long id;
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
    private List<AnimeEpisodeVo> episodes;
}
