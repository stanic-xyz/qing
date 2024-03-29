package cn.chenyunlong.qing.domain.auth.platform.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class PlatformUpdateRequest implements Request {

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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
