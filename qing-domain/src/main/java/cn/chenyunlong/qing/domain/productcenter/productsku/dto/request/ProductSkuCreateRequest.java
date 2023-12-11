package cn.chenyunlong.qing.domain.productcenter.productsku.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class ProductSkuCreateRequest implements Request {
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
