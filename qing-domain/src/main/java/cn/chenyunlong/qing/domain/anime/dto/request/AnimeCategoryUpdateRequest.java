package cn.chenyunlong.qing.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AnimeCategoryUpdateRequest implements Request {
    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "orderNo"
    )
    private Integer orderNo;

    @Schema(
        title = "pid"
    )
    private Long pid;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
