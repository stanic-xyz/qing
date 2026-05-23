package cn.chenyunlong.qing.auth.infrastructure.initializer;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.*;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 认证数据初始化器
 * 在应用启动时自动初始化必要的角色、权限和超级管理员账户
 *
 * @author System
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * 超级管理员用户名
     */
    private static final String SUPER_ADMIN_USERNAME = "admin";

    /**
     * 超级管理员密码（生产环境应使用更复杂的密码）
     */
    private static final String SUPER_ADMIN_PASSWORD = "Admin123";

    /**
     * 超级管理员昵称
     */
    private static final String SUPER_ADMIN_NICKNAME = "系统管理员";

    /**
     * 超级管理员角色编码
     */
    private static final String SUPER_ADMIN_ROLE_CODE = "SUPER_ADMIN";

    /**
     * 超级管理员角色名称
     */
    private static final String SUPER_ADMIN_ROLE_NAME = "超级管理员";

    /**
     * 普通用户角色编码
     */
    private static final String USER_ROLE_CODE = "USER";

    /**
     * 普通用户角色名称
     */
    private static final String USER_ROLE_NAME = "普通用户";

    @Override
    @Transactional
    public void run(String... args) {
        log.info("========================================");
        log.info("开始检查认证数据初始化状态...");
        log.info("========================================");

        try {
            if (isAlreadyInitialized()) {
                log.info("认证数据已初始化，跳过初始化过程");
                return;
            }

            log.info("系统未初始化，开始初始化认证数据...");

            // 1. 初始化角色
            initializeRoles();

            // 2. 初始化权限
            initializePermissions();

            // 3. 为超级管理员角色分配所有权限
            assignAllPermissionsToSuperAdmin();

            // 4. 创建超级管理员用户并分配角色
            createSuperAdminUser();

            log.info("========================================");
            log.info("认证数据初始化完成!");
            log.info("========================================");
        } catch (Exception e) {
            log.error("认证数据初始化失败", e);
            throw e;
        }
    }

    /**
     * 检查系统是否已初始化
     * 通过检查关键数据是否存在来判断
     */
    private boolean isAlreadyInitialized() {
        if (roleJpaRepository.count() > 0) {
            log.info("检测到系统中已有角色数据，判定为已初始化");
            return true;
        }
        return false;
    }

    /**
     * 初始化角色
     */
    private void initializeRoles() {
        log.info("正在初始化角色...");

        // 创建超级管理员角色
        if (!roleRepository.existsByCode(SUPER_ADMIN_ROLE_CODE)) {
            Role superAdminRole = Role.create(
                    SUPER_ADMIN_ROLE_NAME,
                    SUPER_ADMIN_ROLE_CODE,
                    "系统超级管理员，拥有所有权限",
                    "SYSTEM"
            );
            roleRepository.save(superAdminRole);
            log.info("创建超级管理员角色: {}", SUPER_ADMIN_ROLE_NAME);
        } else {
            log.info("超级管理员角色已存在，跳过创建");
        }

        // 创建普通用户角色
        if (!roleRepository.existsByCode(USER_ROLE_CODE)) {
            Role userRole = Role.create(
                    USER_ROLE_NAME,
                    USER_ROLE_CODE,
                    "普通用户角色",
                    "SYSTEM"
            );
            roleRepository.save(userRole);
            log.info("创建普通用户角色: {}", USER_ROLE_NAME);
        } else {
            log.info("普通用户角色已存在，跳过创建");
        }
    }

    /**
     * 初始化权限
     */
    private void initializePermissions() {
        log.info("正在初始化权限...");

        // 用户管理权限
        createPermissionIfNotExists("user:create", "创建用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:read", "查看用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:update", "更新用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:delete", "删除用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:list", "用户列表", PermissionType.OPERATION);
        createPermissionIfNotExists("user:activate", "激活用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:deactivate", "禁用用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:lock", "锁定用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:unlock", "解锁用户", PermissionType.OPERATION);
        createPermissionIfNotExists("user:assign-role", "分配角色", PermissionType.OPERATION);
        createPermissionIfNotExists("user:revoke-role", "撤销角色", PermissionType.OPERATION);

        // 角色管理权限
        createPermissionIfNotExists("role:create", "创建角色", PermissionType.OPERATION);
        createPermissionIfNotExists("role:read", "查看角色", PermissionType.OPERATION);
        createPermissionIfNotExists("role:update", "更新角色", PermissionType.OPERATION);
        createPermissionIfNotExists("role:delete", "删除角色", PermissionType.OPERATION);
        createPermissionIfNotExists("role:list", "角色列表", PermissionType.OPERATION);
        createPermissionIfNotExists("role:assign-permission", "分配权限", PermissionType.OPERATION);
        createPermissionIfNotExists("role:revoke-permission", "撤销权限", PermissionType.OPERATION);

        // 权限管理权限
        createPermissionIfNotExists("permission:create", "创建权限", PermissionType.OPERATION);
        createPermissionIfNotExists("permission:read", "查看权限", PermissionType.OPERATION);
        createPermissionIfNotExists("permission:update", "更新权限", PermissionType.OPERATION);
        createPermissionIfNotExists("permission:delete", "删除权限", PermissionType.OPERATION);
        createPermissionIfNotExists("permission:list", "权限列表", PermissionType.OPERATION);

        log.info("权限初始化完成");
    }

    /**
     * 创建权限（如果不存在）
     */
    private void createPermissionIfNotExists(String code, String name, PermissionType type) {
        if (!permissionRepository.existsByCode(code)) {
            Permission permission = Permission.create(
                    name,
                    code,
                    type,
                    code.split(":")[0],
                    code.split(":")[1],
                    "SYSTEM"
            );
            permissionRepository.save(permission);
            log.info("创建权限: {} - {}", code, name);
        }
    }

    /**
     * 为超级管理员角色分配所有权限
     */
    private void assignAllPermissionsToSuperAdmin() {
        log.info("正在为超级管理员角色分配权限...");

        // 使用JpaRepository获取所有角色
        RoleEntity superAdminRoleEntity = roleJpaRepository.findAll().stream()
                .filter(r -> SUPER_ADMIN_ROLE_CODE.equals(r.getCode()))
                .findFirst()
                .orElse(null);

        if (superAdminRoleEntity == null) {
            log.warn("未找到超级管理员角色，跳过权限分配");
            return;
        }

        RoleId superAdminRoleId = RoleId.of(superAdminRoleEntity.getId());

        // 获取所有权限实体
        List<PermissionEntity> allPermissions = permissionJpaRepository.findAll();

        if (allPermissions.isEmpty()) {
            log.warn("未找到任何权限，跳过权限分配");
            return;
        }

        // 为每个权限创建角色-权限关联
        int assignedCount = 0;
        for (PermissionEntity perm : allPermissions) {
            PermissionId permissionId = PermissionId.of(perm.getId());

            // 检查是否已存在关联
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(superAdminRoleId, permissionId)) {
                RolePermission rolePermission = RolePermission.create(superAdminRoleId, permissionId);
                rolePermissionRepository.save(rolePermission);
                assignedCount++;
            }
        }

        log.info("已为超级管理员角色分配/验证 {} 个权限", assignedCount);
    }

    /**
     * 创建超级管理员用户并分配角色
     */
    private void createSuperAdminUser() {
        log.info("正在创建超级管理员用户...");

        Username username = Username.of(SUPER_ADMIN_USERNAME);

        if (userRepository.existsByUsername(username)) {
            log.info("超级管理员用户已存在，检查角色分配...");

            // 获取已存在的用户
            Optional<User> existingUser = userRepository.findByUsername(username);
            existingUser.ifPresent(user -> assignRoleToUser(user, SUPER_ADMIN_ROLE_CODE));
            return;
        }

        // 创建用户
        UserId userId = UserId.generate();
        RawPassword rawPassword = RawPassword.of(SUPER_ADMIN_PASSWORD);

        User user = User.create(userId, username, rawPassword, SUPER_ADMIN_NICKNAME);
        user.setActive(true); // 超级管理员默认激活
        user.setLocked(false); // 超级管理员默认未锁定

        userRepository.save(user);
        log.info("创建超级管理员用户: {}", SUPER_ADMIN_USERNAME);

        // 为用户分配超级管理员角色
        assignRoleToUser(user, SUPER_ADMIN_ROLE_CODE);

        log.info("密码: {}", SUPER_ADMIN_PASSWORD);
        log.info("⚠️  提示：生产环境请立即修改默认密码!");
    }

    /**
     * 为用户分配角色
     *
     * @param user      用户
     * @param roleCode  角色编码
     */
    private void assignRoleToUser(User user, String roleCode) {
        // 获取角色
        RoleEntity roleEntity = roleJpaRepository.findAll().stream()
                .filter(r -> roleCode.equals(r.getCode()))
                .findFirst()
                .orElse(null);

        if (roleEntity == null) {
            log.warn("未找到角色: {}，跳过角色分配", roleCode);
            return;
        }

        RoleId roleId = RoleId.of(roleEntity.getId());
        UserId userId = user.getId();

        // 检查用户是否已有该角色
        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            log.info("用户 {} 已有角色 {}，跳过分配", user.getUsername().value(), roleCode);
            return;
        }

        // 创建用户角色关联
        UserRole userRole = UserRole.create(userId, roleId);
        userRoleRepository.save(userRole);
        log.info("为用户 {} 分配角色: {}", user.getUsername().value(), roleCode);
    }
}
