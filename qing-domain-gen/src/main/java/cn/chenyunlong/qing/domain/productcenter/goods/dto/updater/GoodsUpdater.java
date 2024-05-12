package cn.chenyunlong.qing.domain.productcenter.goods.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.goods.Goods;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class GoodsUpdater {

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

    private Long id;

    public void updateGoods(Goods param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getSkuId()).ifPresent(param::setSkuId);
        Optional.ofNullable(getUniqueCode()).ifPresent(param::setUniqueCode);
        Optional.ofNullable(getPrice()).ifPresent(param::setPrice);
        Optional.ofNullable(getBatchNo()).ifPresent(param::setBatchNo);
        Optional.ofNullable(getStoreId()).ifPresent(param::setStoreId);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
