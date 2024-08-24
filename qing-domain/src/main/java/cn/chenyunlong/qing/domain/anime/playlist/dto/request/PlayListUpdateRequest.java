package cn.chenyunlong.qing.domain.anime.playlist.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlayListUpdateRequest implements Request {

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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
