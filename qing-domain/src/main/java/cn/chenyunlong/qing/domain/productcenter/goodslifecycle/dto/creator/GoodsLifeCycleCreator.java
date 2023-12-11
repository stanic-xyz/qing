package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.creator;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class GoodsLifeCycleCreator {
    @Schema(
            title = "directionType",
            description = "出入库方向"
    )
    private DirectionType directionType;

    @Schema(
            title = "uniqueCode",
            description = "唯一编码"
    )
    private String uniqueCode;

    @Schema(
            title = "inOutStoreType",
            description = "出入库类型"
    )
    private InOutStoreType inOutStoreType;

    @Schema(
            title = "storeId",
            description = "仓库ID"
    )
    private Long storeId;

    @Schema(
            title = "remark",
            description = "备注"
    )
    private String remark;
}
