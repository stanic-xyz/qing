package cn.chenyunlong.qing.domain.auth.platform.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlatformQueryRequest implements Request {
    @Schema(
            title = "name",
            description = "平台名称"
    )
    private String name;
}
