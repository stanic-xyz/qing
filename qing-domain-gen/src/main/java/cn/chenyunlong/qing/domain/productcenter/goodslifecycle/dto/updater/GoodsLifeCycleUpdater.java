package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.GoodsLifeCycle;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class GoodsLifeCycleUpdater {

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

    private Long id;

    public void updateGoodsLifeCycle(GoodsLifeCycle param) {
        Optional.ofNullable(getDirectionType()).ifPresent(param::setDirectionType);
        Optional.ofNullable(getUniqueCode()).ifPresent(param::setUniqueCode);
        Optional.ofNullable(getInOutStoreType()).ifPresent(param::setInOutStoreType);
        Optional.ofNullable(getStoreId()).ifPresent(param::setStoreId);
        Optional.ofNullable(getRemark()).ifPresent(param::setRemark);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
