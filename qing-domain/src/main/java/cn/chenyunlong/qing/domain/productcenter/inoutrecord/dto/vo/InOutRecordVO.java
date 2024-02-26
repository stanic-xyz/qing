package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecord;
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
public class InOutRecordVO extends AbstractBaseJpaVo {

    @Schema(
        title = "storeId",
        description = "仓库ID"
    )
    private Long storeId;

    @Schema(
        title = "inOutStoreType",
        description = "出入库类型"
    )
    private InOutStoreType inOutStoreType;

    @Schema(
        title = "directionType",
        description = "出入方向"
    )
    private DirectionType directionType;

    @Schema(
        title = "batchNo",
        description = "批次号"
    )
    private String batchNo;

    @Schema(
        title = "count",
        description = "数量"
    )
    private Integer count;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    @Schema(
        title = "operateUser",
        description = "操作人"
    )
    private String operateUser;

    public InOutRecordVO(InOutRecord source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setStoreId(source.getStoreId());
        this.setInOutStoreType(source.getInOutStoreType());
        this.setDirectionType(source.getDirectionType());
        this.setBatchNo(source.getBatchNo());
        this.setCount(source.getCount());
        this.setValidStatus(source.getValidStatus());
        this.setOperateUser(source.getOperateUser());
    }
}
