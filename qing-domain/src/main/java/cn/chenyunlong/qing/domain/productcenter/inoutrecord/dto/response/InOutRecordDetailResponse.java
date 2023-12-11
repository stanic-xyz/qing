package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class InOutRecordDetailResponse extends AbstractJpaResponse {
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

    @Schema(
            title = "validStatus",
            description = "validStatus"
    )
    private ValidStatus validStatus;
}
