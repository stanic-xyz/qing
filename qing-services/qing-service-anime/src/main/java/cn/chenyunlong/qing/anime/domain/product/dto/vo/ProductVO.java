package cn.chenyunlong.qing.anime.domain.product.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.anime.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
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
public class ProductVO extends AbstractBaseJpaVo {
    @Schema(
            title = "name",
            description = "name"
    )
    private String name;

    @Schema(
            title = "description",
            description = "description"
    )
    private String description;

    @Schema(
            title = "price",
            description = "price"
    )
    private BigDecimal price;

    @Schema(
            title = "stock",
            description = "stock"
    )
    private Integer stock;

    public ProductVO(Product source) {
        super();
        this.setId(source.getId().getId());;
        this.setCreatedAt(source.getCreatedAt());;
        this.setUpdatedAt(source.getCreatedAt());;
        this.setVersion(source.getVersion());;
        this.setName(source.getName());
        this.setDescription(source.getDescription());
        this.setPrice(source.getPrice());
        this.setStock(source.getStock());
    }
}
