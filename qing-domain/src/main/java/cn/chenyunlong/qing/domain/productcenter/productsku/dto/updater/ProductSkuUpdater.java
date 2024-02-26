package cn.chenyunlong.qing.domain.productcenter.productsku.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.productsku.ProductSku;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class ProductSkuUpdater {

    @Schema(
        title = "productId",
        description = "产品ID"
    )
    private Long productId;

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "code",
        description = "编号"
    )
    private String code;

    @Schema(
        title = "skuData",
        description = "sku信息"
    )
    private String skuData;

    private Long id;

    public void updateProductSku(ProductSku param) {
        Optional.ofNullable(getProductId()).ifPresent(param::setProductId);
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getSkuData()).ifPresent(param::setSkuData);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
