package cn.chenyunlong.qing.domain.anime.playlist.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PUBLIC
)
public class PlayListVO extends AbstractBaseJpaVo {

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
