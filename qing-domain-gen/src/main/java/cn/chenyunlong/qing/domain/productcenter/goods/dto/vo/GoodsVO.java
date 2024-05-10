package cn.chenyunlong.qing.domain.productcenter.goods.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.goods.Goods;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class GoodsVO extends AbstractBaseJpaVo {

    @Schema(
        title = "name",
        description = "name"
    )
    private String name;

    @Schema(
        title = "skuId",
        description = "规格ID"
    )
    private Long skuId;

    @Schema(
        title = "uniqueCode",
        description = "唯一编码"
    )
    private String uniqueCode;

    @Schema(
        title = "price",
        description = "价格"
    )
    private BigDecimal price;

    @Schema(
        title = "batchNo",
        description = "批次号"
    )
    private String batchNo;

    @Schema(
        title = "storeId",
        description = "仓库ID"
    )
    private Long storeId;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public GoodsVO(Goods source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setSkuId(source.getSkuId());
        this.setUniqueCode(source.getUniqueCode());
        this.setPrice(source.getPrice());
        this.setBatchNo(source.getBatchNo());
        this.setStoreId(source.getStoreId());
        this.setValidStatus(source.getValidStatus());
    }
}
