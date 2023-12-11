package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class InOutRecordCreateRequest implements Request {
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
}
