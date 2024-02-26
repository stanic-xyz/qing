package cn.chenyunlong.qing.domain.anime.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class AnimeCategoryUpdater {

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

    private Long id;

    public void updateAnimeCategory(AnimeCategory param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getOrderNo()).ifPresent(param::setOrderNo);
        Optional.ofNullable(getPid()).ifPresent(param::setPid);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
