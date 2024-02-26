package cn.chenyunlong.qing.domain.productcenter.productsku.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.productsku.ProductSku;
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
public class ProductSkuVO extends AbstractBaseJpaVo {

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

    public ProductSkuVO(ProductSku source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setProductId(source.getProductId());
        this.setName(source.getName());
        this.setCode(source.getCode());
        this.setSkuData(source.getSkuData());
        this.setValidStatus(source.getValidStatus());
    }
}
