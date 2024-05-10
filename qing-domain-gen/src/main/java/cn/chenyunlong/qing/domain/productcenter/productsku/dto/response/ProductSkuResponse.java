package cn.chenyunlong.qing.domain.productcenter.productsku.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class ProductSkuResponse extends AbstractJpaResponse {

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
