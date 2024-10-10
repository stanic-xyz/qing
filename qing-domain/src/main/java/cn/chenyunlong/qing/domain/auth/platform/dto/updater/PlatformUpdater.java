package cn.chenyunlong.qing.domain.auth.platform.dto.updater;

import cn.chenyunlong.qing.domain.auth.platform.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class PlatformUpdater {

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

    public void updatePlatform(Platform param) {
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getName()).ifPresent(param::setName);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
