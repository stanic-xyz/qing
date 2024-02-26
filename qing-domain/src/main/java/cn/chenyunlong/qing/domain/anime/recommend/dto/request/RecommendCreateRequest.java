package cn.chenyunlong.qing.domain.anime.recommend.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class RecommendCreateRequest implements Request {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    @NotNull
    private Long animeId;
}
