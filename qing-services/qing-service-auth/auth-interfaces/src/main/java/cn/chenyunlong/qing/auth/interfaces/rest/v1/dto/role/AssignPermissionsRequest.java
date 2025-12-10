package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 角色批量关联权限请求
 */
@Data
public class AssignPermissionsRequest {

    /**
     * 待关联的权限ID列表
     */
    @Schema(description = "权限ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private List<Long> permissionIds;
}
