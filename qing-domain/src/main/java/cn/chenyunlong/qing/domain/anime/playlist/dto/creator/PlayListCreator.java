package cn.chenyunlong.qing.domain.anime.playlist.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlayListCreator {

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
