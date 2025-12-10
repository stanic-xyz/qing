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
import cn.chenyunlong.qing.auth.domain.user.command.RevokeRoleCommand;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleAssignmentService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;


    /**
     * 为用户分配角色的方法
     *
     * @param command 包含用户ID和角色ID的分配角色命令对象
     */
    public void assignRole(AssignRoleCommand command) {
        // 根据命令中的用户ID查找用户，如果不存在则抛出"用户未找到"的运行时异常
        User user = userRepository.findById(command.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        // 根据命令中的角色ID查找角色，如果不存在则抛出"角色未找到"的运行时异常
        Role role = roleRepository.findById(command.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));

        // 检查是否已经分配
        if (userRoleRepository.existsByUserIdAndRoleId(user.getId(), role.getId())) {
            throw new IllegalArgumentException("用户已经拥有该角色");
        }

        // 创建一个新的用户角色关联对象，使用用户ID和角色ID
        UserRole userRole = UserRole.create(user.getId(), role.getId());
        // 保存用户角色关联到数据库
        userRoleRepository.save(userRole);
    }

    /**
     * 为用户分配角色的方法
     *
     * @param command 包含用户ID和角色ID的分配角色命令对象
     */
    public void revokeRoleForUser(RevokeRoleCommand command) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(command.getUserId(), command.getRoleId()).orElseThrow(() -> new RuntimeException("用户未拥有该角色"));
        userRole.revoke();
        // 创建一个新的用户角色关联对象，使用用户ID和角色ID
        // 保存用户角色关联到数据库
        userRoleRepository.save(userRole);
    }

    /**
     * 为用户分配角色的方法
     *
     * @param command 包含用户ID和角色ID的分配角色命令对象
     */
    public void assignPermissionToRole(AssignPermissionCommand command) {
        // 根据命令中的角色ID查找角色，如果不存在则抛出"角色未找到"的运行时异常
        Role role = roleRepository.findById(command.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(command.getPermissionId()).orElseThrow(() -> new RuntimeException(""));

        // 检查是否已经分配
        if (rolePermissionRepository.existsByRoleIdAndPermissionId(role.getId(), permission.getId())) {
            throw new IllegalArgumentException("用户已经拥有该角色");
        }

        // 创建一个新的用户角色关联对象，使用用户ID和角色ID
        RolePermission userRole = RolePermission.create(command.getRoleId(), command.getPermissionId());
        // 保存用户角色关联到数据库
        rolePermissionRepository.save(userRole);
    }
}
