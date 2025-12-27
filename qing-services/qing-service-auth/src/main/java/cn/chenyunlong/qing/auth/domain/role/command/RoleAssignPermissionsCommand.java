package cn.chenyunlong.qing.auth.domain.role.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

// 命令对象，表示用户的意图
@Data
@Builder
public class RoleAssignPermissionsCommand {
    @NotNull
    private RoleId roleId;

    @NotNull
    @Size(min = 1, message = "至少需要一个权限")
    private List<PermissionId> permissionIds;

    @NotBlank
    private String assignedBy; // 操作人
}
