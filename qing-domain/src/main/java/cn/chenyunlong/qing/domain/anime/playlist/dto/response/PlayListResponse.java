package cn.chenyunlong.qing.domain.anime.playlist.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class PlayListResponse extends AbstractJpaResponse {

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
