package cn.chenyunlong.qing.domain.anime.episode.dto.vo;

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
public class EpisodeVO extends AbstractBaseJpaVo {

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "animeId",
        description = "动漫ID"
    )
    private Long animeId;

    private String playUrl;

    @Schema(
        title = "collectionId",
        description = "播放源ID"
    )
    private Long playListId;

    @Schema(
        title = "collectionName",
        description = "播放源名称"
    )
    private String description;
}
