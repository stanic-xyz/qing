package cn.chenyunlong.qing.domain.anime.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class AnimeCategoryResponse extends AbstractJpaResponse {
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
