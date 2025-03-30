package cn.chenyunlong.qing.anime.domain.episode.dto.creator;

import cn.chenyunlong.common.annotation.FieldDesc;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class EpisodeCreator {

    @Schema(title = "name", description = "名称")
    private String name;

    private String description;

    @Schema(title = "animeId", description = "动漫ID")
    private Long animeId;

    @Schema(title = "播放列表")
    private Long playListId;

    @Schema(title = "playUrl", description = "播放地址")
    private String playUrl;

    @FieldDesc(name = "集数", description = "集数")
    private Integer episodeNumber;
}
