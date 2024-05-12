package cn.chenyunlong.qing.domain.auth.platform.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlatformQuery {

    @Schema(
        title = "name",
        description = "平台名称"
    )
    private String name;
}
