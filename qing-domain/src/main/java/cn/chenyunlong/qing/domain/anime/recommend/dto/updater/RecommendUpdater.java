package cn.chenyunlong.qing.domain.anime.recommend.dto.updater;

import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class RecommendUpdater {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    private Long id;

    public void updateRecommend(Recommend param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getInstruction()).ifPresent(param::setInstruction);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
