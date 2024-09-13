package cn.chenyunlong.qing.domain.auth.role.dto.updater;

import cn.chenyunlong.qing.domain.auth.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class RoleUpdater {

    private Long id;

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

    public void updateRole(Role param) {
        Optional.ofNullable(getRole()).ifPresent(param::setRole);
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getPlatformId()).ifPresent(param::setPlatformId);
        Optional.ofNullable(getRemark()).ifPresent(param::setRemark);
    }
}
