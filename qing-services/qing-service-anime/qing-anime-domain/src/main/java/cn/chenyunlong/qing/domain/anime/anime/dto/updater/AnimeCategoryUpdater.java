package cn.chenyunlong.qing.domain.anime.anime.dto.updater;

import cn.chenyunlong.qing.domain.anime.anime.Category;
import cn.hutool.core.lang.Assert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

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

    public void updateAnimeCategory(Category param) {
        Assert.notEquals(param.getPid(), getId(), "父级分类设置自己为父级分类");
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getOrderNo()).ifPresent(param::setOrderNo);
        Optional.ofNullable(getPid()).ifPresent(param::setPid);
    }
}
