package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class InOutRecordDetailCreateRequest implements Request {

    @Schema(
        title = "uniqueCodes",
        description = "唯一编码字符串，约定用;分隔"
    )
    private String uniqueCodes;

    @Schema(
        title = "extInfo",
        description = "扩展信息"
    )
    private String extInfo;
}
