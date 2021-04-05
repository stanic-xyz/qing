package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodeDTO implements OutputConverter<EpisodeDTO, AnimeEpisodeEntity> {
    private Long id;
    private Long animeId;
    private Long playlistId;
    private String name;
    private Integer status;
    private String uploaderName;
    private Long uploaderId;
    private LocalDateTime uploadTime;
    private String url1;
    private String url3;
    private String url2;
    private Integer orderNo;
}
