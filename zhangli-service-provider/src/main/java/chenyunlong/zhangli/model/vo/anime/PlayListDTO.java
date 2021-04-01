package chenyunlong.zhangli.model.vo.anime;

import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class PlayListDTO {
    private Long id;
    private String name;
    private String description;
    private List<AnimeEpisodeEntity> playList;
}
