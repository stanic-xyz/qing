package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionType;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.exception.PermissionNotFoundException;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.command.RemovePermissionFromRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.RoleAssignPermissionsCommand;
import cn.chenyunlong.qing.auth.domain.role.event.PermissionsAssignedToRoleEvent;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        role = Role.create("Test Role", "test_role", "Description", "admin");
        permission = Permission.create("Test Permission", "test:perm", PermissionType.MENU, "resource", "action", "admin");
    }

    @Test
    @DisplayName("为角色分配权限 - 成功")
    void assignPermissionsToRole_Success() {
        // Arrange
        RoleId roleId = role.getId();
        PermissionId permissionId = permission.getId();
        List<PermissionId> permissionIds = Collections.singletonList(permissionId);

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(roleId)
                .permissionIds(permissionIds)
                .assignedBy("admin")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findByIds(permissionIds)).thenReturn(Collections.singletonList(permission));

        // Act
        authApplicationService.assignPermissionsToRole(command);

        // Assert
        verify(roleRepository).save(role);
        // 验证 role 对象中确实包含了该权限
        assert role.getPermissionIds().contains(permissionId);
        // 验证发布了事件
        // 验证事件发布，并捕获事件参数进行验证
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(applicationEventPublisher, times(1)).publishEvent(eventCaptor.capture());

        Object event = eventCaptor.getValue();
        assertInstanceOf(PermissionsAssignedToRoleEvent.class, event);
        PermissionsAssignedToRoleEvent assignedEvent = (PermissionsAssignedToRoleEvent) event;

        assertEquals(1, assignedEvent.getPermissionIds().size());
        assertTrue(assignedEvent.getPermissionIds().contains(permissionId));

    }

    @Test
    @DisplayName("为角色分配权限 - 权限已存在 - 不发布事件")
    void assignPermissionsToRole_Duplicate_NoEvent() {
        // Arrange
        RoleId roleId = role.getId();
        PermissionId permissionId = permission.getId();
        List<PermissionId> permissionIds = Collections.singletonList(permissionId);

        // 先分配一次
        role.assignPermissions(permissionIds);
        // 清除之前的事件
        role.clearDomainEvents();

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(roleId)
                .permissionIds(permissionIds)
                .assignedBy("admin")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findByIds(permissionIds)).thenReturn(Collections.singletonList(permission));

        // Act
        authApplicationService.assignPermissionsToRole(command);

        // Assert
        verify(roleRepository).save(role);
        // 验证 role 对象中仍然包含该权限
        assert role.getPermissionIds().contains(permissionId);
        // 验证没有发布事件 (注意：authApplicationService 中遍历 role.domainEvents() 发布，如果 role 没有产生事件，就不会调用 publishEvent)
        verify(applicationEventPublisher, never()).publishEvent(any(Object.class));
    }

    @Test
    @DisplayName("为角色分配权限 - 部分新增 - 仅发布新增事件")
    void assignPermissionsToRole_PartialNew_EventContainsOnlyNew() {
        // Arrange
        RoleId roleId = role.getId();
        PermissionId existingPermId = permission.getId();
        Permission newPerm = Permission.create("New Permission", "new:perm", PermissionType.OPERATION, "resource", "action", "admin");
        PermissionId newPermId = newPerm.getId();

        // 先分配已有的
        role.assignPermissions(Collections.singletonList(existingPermId));
        // 确保第一次分配成功
        assert role.getPermissionIds().contains(existingPermId);

        role.clearDomainEvents();

        List<PermissionId> allPermissionIds = new ArrayList<>();
        allPermissionIds.add(existingPermId);
        allPermissionIds.add(newPermId);

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(roleId)
                .permissionIds(allPermissionIds)
                .assignedBy("admin")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findByIds(allPermissionIds)).thenReturn(List.of(permission, newPerm));

        // Act
        authApplicationService.assignPermissionsToRole(command);

        // Assert
        verify(roleRepository).save(role);
        // 验证事件发布，并捕获事件参数进行验证
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(applicationEventPublisher, times(1)).publishEvent(eventCaptor.capture());

        Object event = eventCaptor.getValue();
        assertInstanceOf(PermissionsAssignedToRoleEvent.class, event);
        PermissionsAssignedToRoleEvent assignedEvent = (PermissionsAssignedToRoleEvent) event;

        assertEquals(1, assignedEvent.getPermissionIds().size());
        assertTrue(assignedEvent.getPermissionIds().contains(newPermId));
    }

    @Test
    @DisplayName("为角色分配权限 - 角色不存在")
    void assignPermissionsToRole_RoleNotFound() {
        // Arrange
        RoleId roleId = RoleId.of(999L);
        List<PermissionId> permissionIds = Collections.singletonList(PermissionId.of(1L));

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(roleId)
                .permissionIds(permissionIds)
                .assignedBy("admin")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authApplicationService.assignPermissionsToRole(command));
        verify(roleRepository, never()).save(any());
    }

    @Test
    @DisplayName("为角色分配权限 - 权限不存在")
    void assignPermissionsToRole_PermissionNotFound() {
        // Arrange
        RoleId roleId = role.getId();
        PermissionId permissionId = PermissionId.of(999L);
        List<PermissionId> permissionIds = Collections.singletonList(permissionId);

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(roleId)
                .permissionIds(permissionIds)
                .assignedBy("admin")
                .build();

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepository.findByIds(permissionIds)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(PermissionNotFoundException.class, () -> authApplicationService.assignPermissionsToRole(command));
        verify(roleRepository, never()).save(any());
    }

    @Test
    @DisplayName("从角色移除权限 - 成功")
    void removePermissionFromRole_Success() {
        // Arrange
        // 先给角色分配权限
        role.assignPermissions(Collections.singletonList(permission.getId()));

        RemovePermissionFromRoleCommand command = new RemovePermissionFromRoleCommand(role.getId(), permission.getId());

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(permissionRepository.findById(permission.getId())).thenReturn(Optional.of(permission));

        // Act
        authApplicationService.removePermissionFromRole(command);

        // Assert
        verify(roleRepository).save(role);
        assert !role.getPermissionIds().contains(permission.getId());
    }

    @Test
    @DisplayName("从角色移除权限 - 关联不存在")
    void removePermissionFromRole_AssociationNotFound() {
        // Arrange
        // 角色没有该权限

        RemovePermissionFromRoleCommand command = new RemovePermissionFromRoleCommand(role.getId(), permission.getId());

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(permissionRepository.findById(permission.getId())).thenReturn(Optional.of(permission));

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authApplicationService.removePermissionFromRole(command));
        verify(roleRepository, never()).save(any());
    }
}
