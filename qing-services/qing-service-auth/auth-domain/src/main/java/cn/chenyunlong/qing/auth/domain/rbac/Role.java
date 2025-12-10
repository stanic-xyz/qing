/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.domain.rbac;

import cn.chenyunlong.qing.auth.domain.platform.PlatformId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.domain.common.AuditInfo;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色聚合根
 *
 * <p>RBAC模型中的角色实体，包含以下特性：</p>
 * <ul>
 *   <li>角色基本信息：名称、编码、描述</li>
 *   <li>角色状态：启用/禁用状态</li>
 *   <li>权限关联：角色拥有的权限集合</li>
 *   <li>层级关系：支持角色继承</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class Role extends BaseSimpleBusinessEntity<RoleId> {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色类型
     */
    private RoleType type;

    /**
     * 角色状态
     */
    private RoleStatus status;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 父角色ID
     */
    private RoleId parentId;

    /**
     * 平台标识
     */
    private PlatformId platformId;

    /**
     * 角色拥有的权限
     */
    private Set<Permission> permissions;

    /**
     * 子角色
     */
    private Set<Role> children;


    /**
     * 构造函数
     */
    private Role() {
        this.permissions = new HashSet<>();
        this.children = new HashSet<>();
        this.status = RoleStatus.ENABLED;
        this.type = RoleType.CUSTOM;
        this.sortOrder = 0;
    }

    /**
     * 创建角色
     *
     * @param name        角色名称
     * @param code        角色编码
     * @param description 角色描述
     * @param createdBy   创建者
     * @return 角色实例
     */
    public static Role create(String name, String code, String description, String createdBy) {
        Role role = new Role();
        role.setId(RoleId.generate());
        role.setName(name);
        role.setCode(code);
        role.setDescription(description);
        role.setAuditInfo(AuditInfo.create(createdBy));
        return role;
    }

    /**
     * 创建系统角色
     *
     * @param roleId      角色ID
     * @param name        角色名称
     * @param code        角色编码
     * @param description 角色描述
     * @return 系统角色实例
     */
    public static Role createSystemRole(RoleId roleId, String name, String code, String description) {
        Role role = create(name, code, description, "SYSTEM");
        role.setType(RoleType.SYSTEM);
        return role;
    }

    /**
     * 更新角色信息
     *
     * @param name        角色名称
     * @param description 角色描述
     * @param updatedBy   更新者
     */
    public void updateInfo(String name, String description, String updatedBy) {
        this.name = name;
        this.description = description;
        this.auditInfo.update(updatedBy);
    }

    /**
     * 启用角色
     *
     * @param updatedBy 更新者
     */
    public void enable(String updatedBy) {
        this.status = RoleStatus.ENABLED;
        this.auditInfo.update(updatedBy);
    }

    /**
     * 禁用角色
     *
     * @param updatedBy 更新者
     */
    public void disable(String updatedBy) {
        this.status = RoleStatus.DISABLED;
        this.auditInfo.update(updatedBy);
    }

    /**
     * 添加权限
     *
     * @param permission 权限
     */
    public void addPermission(Permission permission) {
        if (permission != null) {
            this.permissions.add(permission);
            this.auditInfo.update("");
        }
    }

    /**
     * 移除权限
     *
     * @param permission 权限
     */
    public void removePermission(Permission permission) {
        if (permission != null) {
            this.permissions.remove(permission);
            this.auditInfo.update("");
        }
    }

    /**
     * 批量设置权限
     *
     * @param permissions 权限集合
     */
    public void setPermissions(Set<Permission> permissions) {
        this.permissions.clear();
        if (permissions != null) {
            this.permissions.addAll(permissions);
        }
        this.auditInfo.update("");
    }

    /**
     * 检查是否拥有指定权限
     *
     * @param permission 权限
     * @return 是否拥有权限
     */
    public boolean hasPermission(Permission permission) {
        if (permission == null) {
            return false;
        }

        // 检查直接权限
        if (permissions.contains(permission)) {
            return true;
        }

        // 检查父角色权限（角色继承）
        if (parentId != null) {
            // 这里需要通过领域服务来获取父角色并检查权限
            // 为了避免循环依赖，这个逻辑应该在领域服务中实现
            // TODO
        }

        return false;
    }

    /**
     * 检查是否拥有指定权限编码
     *
     * @param permissionCode 权限编码
     * @return 是否拥有权限
     */
    public boolean hasPermission(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }

        return permissions.stream()
                .anyMatch(permission -> permissionCode.equals(permission.getCode()));
    }

    /**
     * 获取所有权限编码
     *
     * @return 权限编码集合
     */
    public Set<String> getPermissionCodes() {
        Set<String> codes = new HashSet<>();
        for (Permission permission : permissions) {
            codes.add(permission.getCode());
        }
        return codes;
    }

    /**
     * 设置父角色
     *
     * @param parentId 父角色ID
     */
    public void setParent(RoleId parentId) {
        this.parentId = parentId;
        this.auditInfo.update("");
    }

    /**
     * 添加子角色
     *
     * @param childRole 子角色
     */
    public void addChild(Role childRole) {
        if (childRole != null && !childRole.getId().equals(this.getId())) {
            this.children.add(childRole);
            childRole.setParent(this.getId());
        }
    }

    /**
     * 移除子角色
     *
     * @param childRole 子角色
     */
    public void removeChild(Role childRole) {
        if (childRole != null) {
            this.children.remove(childRole);
            childRole.setParent(null);
        }
    }

    /**
     * 检查是否为系统角色
     *
     * @return 是否为系统角色
     */
    public boolean isSystemRole() {
        return RoleType.SYSTEM.equals(this.type);
    }

    /**
     * 检查是否启用
     *
     * @return 是否启用
     */
    public boolean isEnabled() {
        return RoleStatus.ENABLED.equals(this.status);
    }

    /**
     * 检查是否可以删除
     *
     * @return 是否可以删除
     */
    public boolean canDelete() {
        // 系统角色不能删除
        if (isSystemRole()) {
            return false;
        }

        // 有子角色的角色不能删除
        return children.isEmpty();
    }

    /**
     * 设置排序号
     *
     * @param sortOrder 排序号
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        this.auditInfo.update("");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;
        return getId() != null && getId().equals(role.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Role{id=%s, name='%s', code='%s', type=%s, status=%s}",
                getId(), name, code, type, status);
    }
}
