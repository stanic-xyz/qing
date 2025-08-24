package cn.chenyunlong.qing.anime.domain.anime.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PUBLIC
)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AnimeCategoryVO extends AbstractBaseJpaVo {

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
