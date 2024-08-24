package cn.chenyunlong.qing.domain.anime.faverate.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Long;
import java.lang.String;
import lombok.Data;

@Schema
@Data
public class FavoriteCreateRequest implements Request {
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
