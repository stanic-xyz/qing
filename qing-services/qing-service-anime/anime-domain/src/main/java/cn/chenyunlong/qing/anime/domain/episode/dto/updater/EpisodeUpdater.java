package cn.chenyunlong.qing.anime.domain.episode.dto.updater;

import cn.chenyunlong.qing.anime.domain.episode.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class EpisodeUpdater {

    private Long id;

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "description",
        description = "播放源名称"
    )
    private String description;

    @Schema(
        title = "playUrl",
        description = "播放源地址"
    )
    private String playUrl;

    private Integer episodeNumber;

    public void updateEpisode(Episode param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getPlayUrl()).ifPresent(param::setPlayUrl);
        Optional.ofNullable(getEpisodeNumber()).ifPresent(param::setEpisodeNumber);
    }

}
