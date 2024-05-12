package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class InOutRecordDetailCreator {

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
