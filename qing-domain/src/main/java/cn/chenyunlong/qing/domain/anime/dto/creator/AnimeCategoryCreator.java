package cn.chenyunlong.qing.domain.anime.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import lombok.Data;

@Schema
@Data
public class AnimeCategoryCreator {
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
}
