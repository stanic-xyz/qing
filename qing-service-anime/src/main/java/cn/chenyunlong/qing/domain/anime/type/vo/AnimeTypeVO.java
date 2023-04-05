// ---Auto Generated by Qing-Generator ---
package cn.chenyunlong.qing.domain.anime.type.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVO;
import cn.chenyunlong.qing.domain.anime.type.AnimeType;
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
public class AnimeTypeVO extends AbstractBaseJpaVO {
    @Schema(
            title = "Name",
            description = "name"
    )
    private String name;

    @Schema(
            title = "Description",
            description = "description"
    )
    private String description;

    @Schema(
            title = "OrderNo",
            description = "orderNo"
    )
    private Integer orderNo;

    public AnimeTypeVO(AnimeType source) {
        super();
        this.setName(source.getName());
        this.setDescription(source.getDescription());
        this.setOrderNo(source.getOrderNo());
    }
}