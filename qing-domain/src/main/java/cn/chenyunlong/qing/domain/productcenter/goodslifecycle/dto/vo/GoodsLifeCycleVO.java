package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.GoodsLifeCycle;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
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
public class GoodsLifeCycleVO extends AbstractBaseJpaVo {
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

    @Schema(
            title = "validStatus",
            description = "validStatus"
    )
    private ValidStatus validStatus;

    public GoodsLifeCycleVO(GoodsLifeCycle source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setDirectionType(source.getDirectionType());
        this.setUniqueCode(source.getUniqueCode());
        this.setInOutStoreType(source.getInOutStoreType());
        this.setStoreId(source.getStoreId());
        this.setRemark(source.getRemark());
        this.setValidStatus(source.getValidStatus());
    }
}
