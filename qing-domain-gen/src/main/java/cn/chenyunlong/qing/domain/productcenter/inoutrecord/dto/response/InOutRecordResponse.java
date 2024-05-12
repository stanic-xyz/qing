package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class InOutRecordResponse extends AbstractJpaResponse {

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
}
