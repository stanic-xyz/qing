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

package cn.chenyunlong.qing.auth.domain.rbac.permission;

import cn.chenyunlong.qing.auth.domain.rbac.Operator;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.permission.event.PermissionStatusChangedEvent;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionDomainException;
import cn.chenyunlong.qing.auth.domain.rbac.permission.spec.EnablePermissionSpec;
import cn.chenyunlong.qing.domain.common.AuditInfo;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限聚合根
 *
 * <p>RBAC模型中的权限实体，包含以下特性：</p>
 * <ul>
 *   <li>权限基本信息：名称、编码、描述</li>
 *   <li>权限类型：菜单权限、操作权限、数据权限</li>
 *   <li>权限状态：启用/禁用状态</li>
 *   <li>层级关系：支持权限树形结构</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class Permission extends BaseSimpleBusinessEntity<PermissionId> {

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 是否是内置权限
     */
    private Boolean isSystem;

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
     * 父权限 ID
     */
    private PermissionId parentId;

    /**
     * 子权限
     */
    private Set<Permission> children;

    /**
     * 父权限
     */
    private Permission parent;

    /**
     * 构造函数
     */
    protected Permission() {
        this.children = new HashSet<>();
        this.status = PermissionStatus.ENABLED;
        this.type = PermissionType.OPERATION;
        this.sortOrder = 0;
        this.version = 0;
    }

    /**
     * 创建权限
     *
     * @param name      权限名称
     * @param code      权限编码
     * @param type      权限类型
     * @param resource  资源路径
     * @param action    操作动作
     * @param createdBy 创建者
     * @return 权限实例
     */
    public static Permission create(String name, String code,
                                    PermissionType type, String resource, String action, String createdBy) {
        Permission permission = new Permission();
        permission.setId(PermissionId.generate());
        permission.setName(name);
        permission.setCode(code);
        permission.setType(type);
        permission.setResource(resource);
        permission.setAction(action);
        permission.setAuditInfo(AuditInfo.create(createdBy));
        permission.validate();
        return permission;
    }

    /**
     * 创建菜单权限
     *
     * @param permissionId 权限标识
     * @param name         权限名称
     * @param code         权限编码
     * @param resource     菜单路径
     * @param createdBy    创建者
     * @return 菜单权限实例
     */
    public static Permission createMenuPermission(PermissionId permissionId, String name,
                                                  String code, String resource, String createdBy) {
        return create(name, code, PermissionType.MENU, resource, "view", createdBy);
    }

    /**
     * 创建操作权限
     *
     * @param permissionId 权限ID
     * @param name         权限名称
     * @param code         权限编码
     * @param resource     资源路径
     * @param action       操作动作
     * @param createdBy    创建者
     * @return 操作权限实例
     */
    public static Permission createOperationPermission(PermissionId permissionId, String name,
                                                       String code, String resource, String action, String createdBy) {
        return create(name, code, PermissionType.OPERATION, resource, action, createdBy);
    }

    /**
     * 创建数据权限
     *
     * @param permissionId 权限ID
     * @param name         权限名称
     * @param code         权限编码
     * @param resource     数据资源
     * @param action       数据操作
     * @param createdBy    创建者
     * @return 数据权限实例
     */
    public static Permission createDataPermission(PermissionId permissionId, String name,
                                                  String code, String resource, String action, String createdBy) {
        return create(name, code, PermissionType.DATA, resource, action, createdBy);
    }

    /**
     * 更新权限信息
     *
     * @param name        权限名称
     * @param description 权限描述
     * @param resource    资源路径
     * @param action      操作动作
     * @param updatedBy   更新者
     */
    public void updateInfo(String name, String description, String resource, String action, String updatedBy) {
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
        this.auditInfo.update(updatedBy);
        validate();
    }

    // 验证聚合根的完整性
    private void validate() {
        if (this.code == null) {
            throw new PermissionDomainException("权限编码不能为空");
        }

        if (this.name == null || this.name.trim().isEmpty()) {
            throw new PermissionDomainException("权限名称不能为空");
        }

        if (this.type == null) {
            throw new PermissionDomainException("权限类型不能为空");
        }
    }

    /**
     * 启用权限
     *
     * @param updatedBy 更新者
     */
    public void enable(String updatedBy) {
        this.status = PermissionStatus.ENABLED;
        this.auditInfo.update(updatedBy);
    }

    /**
     * 禁用权限
     *
     * @param updatedBy 更新者
     */
    public void disable(String updatedBy) {
        if (this.status == PermissionStatus.DISABLED) {
            throw new IllegalStateException("已被禁用，无法再次禁用！");
        }
        this.status = PermissionStatus.DISABLED;
        this.auditInfo.update(updatedBy);
    }

    // 更新状态 - 核心领域行为
    public void updateStatus(PermissionStatus newStatus, String reason, Operator operator) {
        // 状态不变，直接返回
        if (this.status.equals(newStatus)) {
            throw new PermissionDomainException(String.format("权限已经处于%s状态", newStatus.getDescription()));
        }

        // 验证状态变更规则
        validateStatusChange(newStatus, reason);

        // 记录旧状态
        PermissionStatus oldStatus = this.status;

        // 更新状态
        this.status = newStatus;
        this.auditInfo.update("system");

        // 处理状态变更的副作用
        handleStatusChangeSideEffects(oldStatus, newStatus, reason);

        // 发布领域事件
        registerEvent(new PermissionStatusChangedEvent(this.id, oldStatus, newStatus, reason, operator, Instant.now()));
    }

    // 处理状态变更的副作用
    private void handleStatusChangeSideEffects(PermissionStatus oldStatus,
                                               PermissionStatus newStatus,
                                               String reason) {
        // 如果权限被禁用，清理相关缓存
        if (newStatus == PermissionStatus.DISABLED) {
            clearRelatedCache();
        }

        // 如果从禁用变为启用，重新计算排序等
        if (oldStatus == PermissionStatus.DISABLED && newStatus == PermissionStatus.ENABLED) {
            resetSortIfNeeded();
        }
    }

    private void resetSortIfNeeded() {
        //todo 可能需要重新计算排序
    }

    private void clearRelatedCache() {
        // todo 删除相关的缓存
    }

    // 验证状态变更的业务规则
    private void validateStatusChange(PermissionStatus newStatus, String reason) {
        // 规约模式
        EnablePermissionSpec enableSpec = new EnablePermissionSpec();

        enableSpec.isSatisfiedBy(this);

        // 系统内置权限不能禁用
        if (Boolean.TRUE.equals(isSystem) && newStatus == PermissionStatus.DISABLED) {
            throw new PermissionDomainException("系统内置权限不允许禁用");
        }

        // 如果是禁用操作，检查是否有启用的子权限
        if (newStatus == PermissionStatus.DISABLED) {
            boolean hasEnabledChildren = children.stream()
                    .anyMatch(child -> child.getStatus() == PermissionStatus.ENABLED);
            if (hasEnabledChildren) {
                throw new PermissionDomainException("请先禁用所有子权限");
            }
        }

        // 如果是启用操作，检查父权限状态
        if (newStatus == PermissionStatus.ENABLED && parent != null) {
            if (parent.getStatus() == PermissionStatus.DISABLED) {
                throw new PermissionDomainException("父权限被禁用，无法启用当前权限");
            }
        }
    }

    /**
     * 设置父权限
     *
     * @param parentId 父权限ID
     */
    public void setParent(PermissionId parentId) {
        this.parentId = parentId;
        this.auditInfo.update("");
    }

    /**
     * 添加子权限
     *
     * @param childPermission 子权限
     */
    public void addChild(Permission childPermission) {
        if (childPermission != null && !childPermission.getId().equals(this.getId())) {
            this.children.add(childPermission);
            childPermission.setParent(this.getId());
        }
    }

    /**
     * 移除子权限
     *
     * @param childPermission 子权限
     */
    public void removeChild(Permission childPermission) {
        if (childPermission != null) {
            this.children.remove(childPermission);
            childPermission.setParent(null);
        }
    }

    /**
     * 检查是否启用
     *
     * @return 是否启用
     */
    public boolean isEnabled() {
        return PermissionStatus.ENABLED.equals(this.status);
    }

    /**
     * 检查是否为菜单权限
     *
     * @return 是否为菜单权限
     */
    public boolean isMenuPermission() {
        return PermissionType.MENU.equals(this.type);
    }

    /**
     * 检查是否为操作权限
     *
     * @return 是否为操作权限
     */
    public boolean isOperationPermission() {
        return PermissionType.OPERATION.equals(this.type);
    }

    /**
     * 检查是否为数据权限
     *
     * @return 是否为数据权限
     */
    public boolean isDataPermission() {
        return PermissionType.DATA.equals(this.type);
    }

    /**
     * 判断当前权限是否可以修改
     */
    public boolean canModify() {
        return isEnabled();
    }

    /**
     * 检查是否可以删除
     *
     * @return 是否可以删除
     */
    public boolean canDelete() {
        //todo 有子权限的权限不能删除等等
        return !CollUtil.isNotEmpty(children);
    }

    /**
     * 设置排序号
     *
     * @param sortOrder 排序号
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.auditInfo.update("");
    }

    /**
     * 匹配资源和动作
     *
     * @param resource 资源路径
     * @param action   操作动作
     * @return 是否匹配
     */
    public boolean matches(String resource, String action) {
        if (!isEnabled()) {
            return false;
        }

        // 精确匹配
        if (this.resource.equals(resource) && this.action.equals(action)) {
            return true;
        }

        // 通配符匹配
        if (this.resource.endsWith("*")) {
            String prefix = this.resource.substring(0, this.resource.length() - 1);
            if (resource.startsWith(prefix) && this.action.equals(action)) {
                return true;
            }
        }

        // 动作通配符匹配
        return "*".equals(this.action) && this.resource.equals(resource);
    }

    /**
     * 获取完整的权限标识
     *
     * @return 权限标识
     */
    public String getFullPermission() {
        return resource + ":" + action;
    }


    @Override
    public String toString() {
        return String.format("Permission{id=%s, name='%s', code='%s', type=%s, resource='%s', action='%s'}",
                getId(), name, code, type, resource, action);
    }

    /**
     * 删除权限信息
     */
    public void delete() {
        // 当前暂时没有可删除的方法！
        throw new NotImplementedException("删除方法未实现");
    }
}
