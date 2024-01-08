package cn.chenyunlong.qing.domain.anime.recommend.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class RecommendResponse extends AbstractJpaResponse {
    @Schema(
            title = "name"
    )
    private String name;

    @Schema(
            title = "instruction"
    )
    private String instruction;

    private AnimeVO animeVO;
}
