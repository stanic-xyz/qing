package cn.chenyunlong.qing.domain.anime.faverate.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class FavoriteCreator {

    @Schema(
        title = "animeId"
    )
    private Long animeId;

    @Schema(
        title = "username",
        description = "username"
    )
    private String username;
}
