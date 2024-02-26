package cn.chenyunlong.qing.domain.auth.platform.dto.response;

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
public class PlatformResponse extends AbstractJpaResponse {

    @Schema(
        title = "code",
        description = "编码"
    )
    private String code;

    @Schema(
        title = "name",
        description = "平台名称"
    )
    private String name;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
