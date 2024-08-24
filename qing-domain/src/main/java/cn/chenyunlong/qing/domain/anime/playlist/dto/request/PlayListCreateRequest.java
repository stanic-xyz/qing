package cn.chenyunlong.qing.domain.anime.playlist.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlayListCreateRequest implements Request {

    @Schema(title = "动漫Id")
    private Long animeId;

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "description"
    )
    private String description;

    @Schema(
        title = "icon"
    )
    private String icon;
}
