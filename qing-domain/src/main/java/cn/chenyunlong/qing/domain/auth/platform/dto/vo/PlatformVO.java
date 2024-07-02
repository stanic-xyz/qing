package cn.chenyunlong.qing.domain.auth.platform.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.platform.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PROTECTED
)
public class PlatformVO extends AbstractBaseJpaVo {

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

    public PlatformVO(Platform source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setVersion(source.getVersion());
        this.setCode(source.getCode());
        this.setName(source.getName());
        this.setValidStatus(source.getValidStatus());
    }
}
