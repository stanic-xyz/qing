package cn.chenyunlong.qing.domain.attribute.dto.request;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.attribute.WebControlType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AttributeCreateRequest implements Request {
    @Schema(
        title = "uniqueCode",
        description = "唯一编码"
    )
    private String uniqueCode;

    @Schema(
        title = "controlType",
        description = "前端控件类型"
    )
    private WebControlType controlType;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
