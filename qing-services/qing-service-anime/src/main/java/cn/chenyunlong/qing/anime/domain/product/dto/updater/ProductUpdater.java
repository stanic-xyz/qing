package cn.chenyunlong.qing.anime.domain.product.dto.updater;

import cn.chenyunlong.qing.anime.domain.product.Product;
import cn.chenyunlong.qing.domain.common.AggregateId;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class ProductUpdater {
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

    private AggregateId id;

    public void updateProduct(Product param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getPrice()).ifPresent(param::setPrice);
        Optional.ofNullable(getStock()).ifPresent(param::setStock);
    }

    public AggregateId getId() {
        return id;
    }

    public void setId(AggregateId id) {
        this.id = id;
    }
}
