package cn.chenyunlong.qing.domain.anime.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.hutool.core.lang.Assert;
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
        Assert.notEquals(param.getPid(), getId(), "父级分类设置自己为父级分类");
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getOrderNo()).ifPresent(param::setOrderNo);
        Optional.ofNullable(getPid()).ifPresent(param::setPid);
    }
}
