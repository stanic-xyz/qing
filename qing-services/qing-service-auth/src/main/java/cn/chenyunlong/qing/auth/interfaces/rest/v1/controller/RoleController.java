package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.command.RemovePermissionFromRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.CreateRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.DeleteRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.command.RoleAssignPermissionsCommand;
import cn.chenyunlong.qing.auth.domain.role.command.UpdateRoleCommand;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.PermissionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.AssignPermissionsRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.CreateRoleRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.UpdateRoleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PermissionJpaRepository permissionJpaRepository;

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

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色")
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<JsonResult<Long>> create(@RequestBody CreateRoleRequest request) {
        CreateRoleCommand command = CreateRoleCommand.create(
                request.getCode(),
                request.getName(),
                request.getDescription()
        );
        cn.chenyunlong.qing.auth.domain.rbac.Role role = authApplicationService.createRole(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonResult.success(role.getId().id()));
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<JsonResult<Void>> update(
            @PathVariable("id") Long id,
            @RequestBody UpdateRoleRequest request) {
        UpdateRoleCommand command = UpdateRoleCommand.builder()
                .id(RoleId.of(id))
                .name(request.getName())
                .description(request.getDescription())
                .build();
        authApplicationService.updateRole(command);
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable("id") Long id) {
        DeleteRoleCommand command = DeleteRoleCommand.builder()
                .id(RoleId.of(id))
                .build();
        authApplicationService.deleteRole(command);
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 分页查询角色列表
     */
    @GetMapping
    @Operation(summary = "获取角色列表", description = "分页查询角色列表")
    @PreAuthorize("hasAuthority('role:list')")
    public JsonResult<Page<RoleEntity>> list(
            @Parameter(description = "页码", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "排序字段", example = "id,desc")
            @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<RoleEntity> result = roleJpaRepository.findAll(pageable);
        return JsonResult.success(result);
    }

    /**
     * 获取角色的权限列表
     */
    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取角色的权限列表")
    @PreAuthorize("hasAuthority('role:read')")
    public JsonResult<List<Permission>> getRolePermissions(@PathVariable("id") Long id) {
        RoleId roleId = RoleId.of(id);
        List<Permission> permissions = authApplicationService.getPermissionsByRoleId(roleId);
        return JsonResult.success(permissions);
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
    @PreAuthorize("hasAuthority('role:assign-permission')")
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
    @PreAuthorize("hasAuthority('role:revoke-permission')")
    public ResponseEntity<JsonResult<Void>> revokePermission(
            @PathVariable("roleId") @Parameter(description = "角色ID") Long roleId,
            @PathVariable("permissionId") @Parameter(description = "权限ID") Long permissionId) {
        authApplicationService.removePermissionFromRole(
                RemovePermissionFromRoleCommand.create(roleId, permissionId)
        );
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 解析排序参数
     */
    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}
