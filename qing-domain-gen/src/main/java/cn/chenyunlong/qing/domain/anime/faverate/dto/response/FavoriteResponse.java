package cn.chenyunlong.qing.domain.anime.faverate.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Long;
import java.lang.String;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class FavoriteResponse extends AbstractJpaResponse {
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
