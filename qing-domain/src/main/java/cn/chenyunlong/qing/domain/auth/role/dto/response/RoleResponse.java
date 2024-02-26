package cn.chenyunlong.qing.domain.auth.role.dto.response;

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
public class RoleResponse extends AbstractJpaResponse {

    @Schema(
        title = "role",
        description = "角色编码"
    )
    private String role;

    @Schema(
        title = "name",
        description = "角色名称"
    )
    private String name;

    @Schema(
        title = "platformId",
        description = "平台Id"
    )
    private Long platformId;

    @Schema(
        title = "remark",
        description = "备注"
    )
    private String remark;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
