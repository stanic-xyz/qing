package cn.chenyunlong.qing.domain.auth.platform.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class PlatformUpdateRequest implements Request {

    @NotNull
    private Long id;

    @Schema(title = "code", description = "编码")
    private String code;

    @Schema(title = "name", description = "平台名称")
    private String name;
}
