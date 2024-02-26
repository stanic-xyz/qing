package cn.chenyunlong.qing.domain.auth.role.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class RoleCreator {

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
}
