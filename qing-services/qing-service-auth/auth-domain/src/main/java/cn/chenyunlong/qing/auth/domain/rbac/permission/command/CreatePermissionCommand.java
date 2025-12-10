package cn.chenyunlong.qing.auth.domain.rbac.permission.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePermissionCommand {

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限类型
     */
    private PermissionType type;

    /**
     * 权限状态
     */
    private PermissionStatus status;

    /**
     * 资源路径
     */
    private String resource;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 父权限ID
     */
    private PermissionId parentId;
}
