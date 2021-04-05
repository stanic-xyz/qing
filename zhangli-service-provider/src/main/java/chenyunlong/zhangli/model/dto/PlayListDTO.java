package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.AnimePlaylistEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class PlayListDTO implements OutputConverter<PlayListDTO, AnimePlaylistEntity> {
    private Long id;
    private Long animeId;
    private String name;
    private String description;
}
