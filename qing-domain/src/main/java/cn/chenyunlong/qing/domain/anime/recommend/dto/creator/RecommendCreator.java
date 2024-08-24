package cn.chenyunlong.qing.domain.anime.recommend.dto.creator;

import cn.chenyunlong.common.annotation.FieldDesc;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class RecommendCreator {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    private Long animeId;

    @FieldDesc(description = "推荐人")
    private String recommendUserId;
}
