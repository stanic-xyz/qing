package cn.chenyunlong.qing.domain.anime.type.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
public class TypeVO extends AbstractBaseJpaVo {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

}
