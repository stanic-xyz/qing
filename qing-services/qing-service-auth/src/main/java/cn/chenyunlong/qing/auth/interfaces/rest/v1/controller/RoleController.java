package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.command.RemovePermissionFromRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.CreateRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.RoleAssignPermissionsCommand;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.AssignPermissionsRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.CreateRoleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "角色管理", description = "角色管理相关接口")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleRepository roleRepository;
    private final UserDomainService userDomainService;
    private final AuthApplicationService authApplicationService;
    private final RoleJpaRepository roleJpaRepository;

    /**
     * 获取角色信息
     *
     * @param roleId 角色id
     * @return 角色信息
     */
    @GetMapping("/{roleId}")
    @Operation(summary = "获取角色信息", description = "根据角色ID获取角色信息")
    @PreAuthorize("hasAuthority('role:read')")
    public JsonResult<RoleEntity> getRoleById(@PathVariable("roleId") Long roleId) {
        Optional<RoleEntity> byId = roleJpaRepository.findById(roleId);
        return byId.map(JsonResult::success).orElseGet(() -> JsonResult.fail("角色不存在"));
    }

    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasAuthority('role:create')")
    public JsonResult<Void> create(@RequestBody CreateRoleRequest request) {
        authApplicationService
                .createRole(CreateRoleCommand.create(request.getCode(), request.getName(), request.getDescription()));
        return JsonResult.success();
    }

    /**
     * 为角色批量关联权限
     *
     * @param roleId  角色ID
     * @param request 权限ID列表请求
     * @return 操作结果
     */
    @PostMapping("/{id}/permissions")
    @Operation(summary = "角色关联权限")
    @PreAuthorize("hasAuthority('role:assign-permissions')")
    public ResponseEntity<JsonResult<Void>> assignPermissions(
            @PathVariable("id") @Parameter(description = "角色标识") Long roleId,
            @RequestBody AssignPermissionsRequest request) {

        RoleAssignPermissionsCommand command = RoleAssignPermissionsCommand.builder()
                .roleId(RoleId.of(roleId))
                .permissionIds(request.getPermissionIds().stream().map(PermissionId::of).toList())
                .build();

        authApplicationService.assignPermissionsToRole(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonResult.success());
    }

    /**
     * 取消角色与权限的关联
     *
     * @param roleId       角色标识
     * @param permissionId 权限标识
     * @return 操作结果
     */
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @Operation(summary = "取消角色权限关联")
    @PreAuthorize("hasAuthority('role:revoke-permissions')")
    public ResponseEntity<JsonResult<Void>> revokePermission(
            @PathVariable("roleId") @Parameter(description = "角色ID") Long roleId,
            @PathVariable("permissionId") @Parameter(description = "权限ID") Long permissionId) {
        authApplicationService.removePermissionFromRole(RemovePermissionFromRoleCommand.create(roleId, permissionId));
        return ResponseEntity.ok(JsonResult.success());
    }
}
