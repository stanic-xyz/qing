package cn.chenyunlong.qing.auth.infrastructure.initializer;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AuthDataInitializer 单元测试
 * 测试数据初始化器的各项功能
 *
 * @author System
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证数据初始化器测试")
class AuthDataInitializerTest {

    @Mock
    private cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository roleRepository;

    @Mock
    private cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository permissionRepository;

    @Mock
    private cn.chenyunlong.qing.auth.domain.user.repository.UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleJpaRepository roleJpaRepository;

    @Mock
    private PermissionJpaRepository permissionJpaRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @InjectMocks
    private AuthDataInitializer authDataInitializer;

    @BeforeEach
    void setUp() {
        // 初始化设置
    }

    @Test
    @DisplayName("测试角色不存在时创建")
    void testCreateRoleWhenNotExists() {
        // Given: 角色不存在
        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(false);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(any())).thenReturn(true);
        when(roleJpaRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证超级管理员角色被创建
        verify(roleRepository, times(1)).save(any(cn.chenyunlong.qing.auth.domain.rbac.Role.class));
    }

    @Test
    @DisplayName("测试角色已存在时跳过创建")
    void testSkipRoleCreationWhenExists() {
        // Given: 角色已存在
        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(any())).thenReturn(true);
        when(roleJpaRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证角色未被创建
        verify(roleRepository, never()).save(any(cn.chenyunlong.qing.auth.domain.rbac.Role.class));
    }

    @Test
    @DisplayName("测试权限不存在时创建")
    void testCreatePermissionWhenNotExists() {
        // Given: user:create权限不存在
        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode("user:create")).thenReturn(false);
        when(permissionRepository.existsByCode(anyString())).thenAnswer(invocation -> {
            String code = invocation.getArgument(0);
            return !code.equals("user:create");
        });
        when(roleJpaRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证权限被创建
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test
    @DisplayName("测试权限已存在时跳过创建")
    void testSkipPermissionCreationWhenExists() {
        // Given: 所有权限已存在
        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(anyString())).thenReturn(true);
        when(roleJpaRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证权限未被创建
        verify(permissionRepository, never()).save(any(Permission.class));
    }

    @Test
    @DisplayName("测试超级管理员用户不存在时创建并分配角色")
    void testCreateSuperAdminWhenNotExists() {
        // Given: 超级管理员用户不存在
        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity roleEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setCode("SUPER_ADMIN");

        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(roleJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(roleEntity));

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证用户被创建
        verify(userRepository, times(1)).save(any(cn.chenyunlong.qing.auth.domain.user.User.class));
        // 验证角色被分配
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    @DisplayName("测试超级管理员用户已存在时检查角色分配")
    void testSuperAdminUserExistsAssignRole() {
        // Given: 超级管理员用户已存在
        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity roleEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setCode("SUPER_ADMIN");

        cn.chenyunlong.qing.auth.domain.user.User mockUser = mock(cn.chenyunlong.qing.auth.domain.user.User.class);
        when(mockUser.getUsername()).thenReturn(new cn.chenyunlong.qing.auth.domain.user.valueObject.Username("admin"));
        when(mockUser.getId()).thenReturn(cn.chenyunlong.qing.auth.domain.user.valueObject.UserId.of(1L));

        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(mockUser));
        when(userRoleRepository.existsByUserIdAndRoleId(any(), any())).thenReturn(false);
        when(roleJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(roleEntity));

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证用户未被创建
        verify(userRepository, never()).save(any(cn.chenyunlong.qing.auth.domain.user.User.class));
        // 验证角色被分配
        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    @DisplayName("测试分配权限时创建RolePermission关联")
    void testAssignPermissionsCreatesRolePermission() {
        // Given: 超级管理员角色存在，有权限需要分配
        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity roleEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setCode("SUPER_ADMIN");

        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity permEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity();
        permEntity.setId(1L);
        permEntity.setCode("user:create");

        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(roleJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(roleEntity));
        when(permissionJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(permEntity));
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(any(), any())).thenReturn(false);

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证RolePermission被创建
        verify(rolePermissionRepository, times(1)).save(any(RolePermission.class));
    }

    @Test
    @DisplayName("测试权限已分配时跳过创建关联")
    void testSkipRolePermissionWhenAlreadyAssigned() {
        // Given: 权限已分配给超级管理员角色
        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity roleEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setCode("SUPER_ADMIN");

        cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity permEntity =
            new cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.PermissionEntity();
        permEntity.setId(1L);
        permEntity.setCode("user:create");

        when(roleRepository.existsByCode("SUPER_ADMIN")).thenReturn(true);
        when(roleRepository.existsByCode("USER")).thenReturn(true);
        when(permissionRepository.existsByCode(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(any())).thenReturn(true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(roleJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(roleEntity));
        when(permissionJpaRepository.findAll()).thenReturn(java.util.Arrays.asList(permEntity));
        when(rolePermissionRepository.existsByRoleIdAndPermissionId(any(), any())).thenReturn(true);
        when(rolePermissionRepository.findPermissionIdsByRoleId(any())).thenReturn(new HashSet<>());

        // When: 执行初始化
        authDataInitializer.run();

        // Then: 验证RolePermission未被创建
        verify(rolePermissionRepository, never()).save(any(RolePermission.class));
    }
}
