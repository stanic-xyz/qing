package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.AssignPermissionCommand;
import cn.chenyunlong.qing.auth.domain.user.command.AssignRoleCommand;
import cn.chenyunlong.qing.auth.domain.user.command.AssignRolesCommand;
import cn.chenyunlong.qing.auth.domain.user.command.RevokeRoleCommand;
import cn.chenyunlong.qing.auth.domain.user.event.UserRolesChangedEvent;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleAssignmentService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 为用户批量分配角色
     *
     * @param command 包含用户ID和角色ID列表的批量分配命令对象
     */
    public void assignRoles(AssignRolesCommand command) {
        log.info("批量分配角色: userId={}, roleIds={}", command.getUserId().id(), command.getRoleIds());

        User user = userRepository.findById(command.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        for (var roleId : command.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

            if (userRoleRepository.existsByUserIdAndRoleId(user.getId(), role.getId())) {
                log.warn("用户已拥有角色: userId={}, roleId={}", user.getId().id(), role.getId());
                continue;
            }

            UserRole userRole = UserRole.create(user.getId(), role.getId());
            userRoleRepository.save(userRole);
            log.debug("角色分配成功: userId={}, roleId={}", user.getId().id(), role.getId());
        }

        eventPublisher.publishEvent(new UserRolesChangedEvent(
            this,
            command.getUserId(),
            command.getRoleIds().stream().map(r -> r.id()).toList(),
            "角色分配变更"
        ));
        log.info("批量角色分配完成，用户角色变更事件已发布");
    }

    /**
     * 为用户分配单个角色
     *
     * @param command 包含用户ID和角色ID的分配角色命令对象
     */
    public void assignRole(AssignRoleCommand command) {
        User user = userRepository.findById(command.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(command.getRoleId())
            .orElseThrow(() -> new RuntimeException("Role not found"));

        if (userRoleRepository.existsByUserIdAndRoleId(user.getId(), role.getId())) {
            throw new IllegalArgumentException("用户已经拥有该角色");
        }

        UserRole userRole = UserRole.create(user.getId(), role.getId());
        userRoleRepository.save(userRole);

        eventPublisher.publishEvent(new UserRolesChangedEvent(
            this,
            command.getUserId(),
            java.util.List.of(command.getRoleId().id()),
            "角色分配"
        ));
    }

    /**
     * 撤销用户角色
     *
     * @param command 包含用户ID和角色ID的撤销命令对象
     */
    public void revokeRoleForUser(RevokeRoleCommand command) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(command.getUserId(), command.getRoleId())
            .orElseThrow(() -> new RuntimeException("用户未拥有该角色"));
        userRole.revoke();
        userRoleRepository.save(userRole);

        eventPublisher.publishEvent(new UserRolesChangedEvent(
            this,
            command.getUserId(),
            java.util.List.of(command.getRoleId().id()),
            "角色撤销"
        ));
    }

    /**
     * 为角色分配权限
     *
     * @param command 包含角色ID和权限ID的分配权限命令对象
     */
    public void assignPermissionToRole(AssignPermissionCommand command) {
        Role role = roleRepository.findById(command.getRoleId())
            .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(command.getPermissionId())
            .orElseThrow(() -> new RuntimeException("Permission not found"));

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId())) {
            throw new IllegalArgumentException("角色已经拥有该权限");
        }

        RolePermission rolePermission = RolePermission.create(command.getRoleId(), command.getPermissionId());
        rolePermissionRepository.save(rolePermission);
    }
}
