package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimeEpisodeDTO implements OutputConverter<AnimeEpisodeDTO, AnimeEpisodeEntity> {

    private Long id;
    private Long animeId;
    private Long playlistId;
    private String name;
    private Integer status;
    private String uploaderName;
    private Long uploaderId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime uploadTime;
    private String url1;
    private String url3;
    private String url2;
    private Integer orderNo;
}
