package cn.chenyunlong.qing.anime.domain.episode.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class EpisodeResponse extends AbstractJpaResponse {

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "animeId",
        description = "动漫ID"
    )
    private Long animeId;

    @Schema(title = "播放列表Id")
    private Long playListId;

    @Schema(title = "播放地址")
    private String playUrl;

}
