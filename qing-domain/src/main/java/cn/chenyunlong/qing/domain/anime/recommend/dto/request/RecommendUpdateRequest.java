package cn.chenyunlong.qing.domain.anime.recommend.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class RecommendUpdateRequest implements Request {
    @Schema(
            title = "name"
    )
    private String name;

    @Schema(
            title = "instruction"
    )
    private String instruction;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
