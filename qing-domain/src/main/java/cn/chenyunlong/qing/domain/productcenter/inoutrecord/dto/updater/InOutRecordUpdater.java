package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecord;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class InOutRecordUpdater {
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
            title = "operateUser",
            description = "操作人"
    )
    private String operateUser;

    private Long id;

    public void updateInOutRecord(InOutRecord param) {
        Optional.ofNullable(getStoreId()).ifPresent(param::setStoreId);
        Optional.ofNullable(getInOutStoreType()).ifPresent(param::setInOutStoreType);
        Optional.ofNullable(getDirectionType()).ifPresent(param::setDirectionType);
        Optional.ofNullable(getBatchNo()).ifPresent(param::setBatchNo);
        Optional.ofNullable(getCount()).ifPresent(param::setCount);
        Optional.ofNullable(getOperateUser()).ifPresent(param::setOperateUser);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
