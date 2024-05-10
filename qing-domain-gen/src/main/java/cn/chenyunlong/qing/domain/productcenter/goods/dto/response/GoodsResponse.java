package cn.chenyunlong.qing.domain.productcenter.goods.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class GoodsResponse extends AbstractJpaResponse {

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
}
