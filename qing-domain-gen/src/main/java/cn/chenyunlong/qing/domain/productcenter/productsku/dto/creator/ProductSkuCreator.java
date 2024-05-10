package cn.chenyunlong.qing.domain.productcenter.productsku.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class ProductSkuCreator {

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
}
