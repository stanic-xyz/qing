package cn.chenyunlong.qing.domain.anime.anime.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
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
    access = AccessLevel.PROTECTED
)
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

    public AnimeCategoryVO(AnimeCategory source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setOrderNo(source.getOrderNo());
        this.setPid(source.getPid());
    }
}
