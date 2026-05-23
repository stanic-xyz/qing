package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.application.service.UserRoleAssignmentService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.*;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.QingUserEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserJpaRepository;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user.CreateUserRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户管理相关接口")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserDomainService userDomainService;
    private final AuthApplicationService authApplicationService;
    private final UserRoleAssignmentService userRoleAssignmentService;
    private final UserJpaRepository userJpaRepository;

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<QingUserEntity> getUserById(@PathVariable("id") Long userId) {
        Optional<QingUserEntity> userOptional = userJpaRepository.findById(userId);
        return userOptional.map(JsonResult::success)
                .orElseGet(() -> JsonResult.fail("用户不存在"));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "编辑用户信息")
    @PreAuthorize("hasAuthority('user:update')")
    public JsonResult<String> updateUser(@PathVariable("id") Long userId, @RequestBody @Valid UpdateUserRequest request) {
        UpdateUserCommand updateUserCommand = UpdateUserCommand.builder()
                .userId(UserId.of(userId))
                .avatar(request.getAvatar())
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .description(request.getDescription())
                .build();
        userDomainService.updateUser(updateUserCommand);
        return JsonResult.success("用户更新成功");
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "激活指定用户")
    @PreAuthorize("hasAuthority('user:activate')")
    public JsonResult<String> activateUser(@PathVariable("userId") Long userId) {
        userDomainService.activeFromAdmin(AdminActiveUserCommand.create(userId, "admin"));
        return JsonResult.success("用户激活成功");
    }

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/deActivate")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    @PreAuthorize("hasAuthority('user:deactivate')")
    public JsonResult<String> deActivate(@PathVariable("userId") Long userId) {
        userDomainService.deActiveFromAdmin(AdminDeActiveUserCommand.create(userId, "admin"));
        return JsonResult.success("用户禁用成功");
    }

    /**
     * 管理员创建用户
     */
    @PostMapping
    @Operation(summary = "管理员创建用户")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<JsonResult<Long>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserRegistrationCommand command = UserRegistrationCommand.create(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getPhone(),
                request.getNickname()
        );
        User user = authApplicationService.register(command);
        Long userId = user.getId().id();
        return ResponseEntity.ok(JsonResult.success(userId));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<JsonResult<Void>> deleteUser(@PathVariable("id") Long userId) {
        // TODO: 实现删除用户逻辑
        return ResponseEntity.ok(JsonResult.success());
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页查询用户列表")
    @PreAuthorize("hasAuthority('user:list')")
    public JsonResult<Page<QingUserEntity>> listUsers(
            @Parameter(description = "页码", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "排序字段", example = "id,desc")
            @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<QingUserEntity> result = userJpaRepository.findAll(pageable);
        return JsonResult.success(result);
    }

    /**
     * 发送更换邮箱验证码
     */
    @PostMapping("/email/code")
    @Operation(summary = "发送更换邮箱验证码")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<Void> sendEmailCode(@RequestBody Object request) {
        // TODO: 实现发送验证码逻辑
        return JsonResult.success();
    }

    /**
     * 确认更换邮箱
     */
    @PostMapping("/email/change")
    @Operation(summary = "确认更换邮箱")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<Void> changeEmail(@RequestBody Object request) {
        // TODO: 实现更换邮箱逻辑
        return JsonResult.success();
    }

    /**
     * 禁用用户（锁定）
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/lock")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    @PreAuthorize("hasAuthority('user:lock')")
    public JsonResult<String> deactivateUser(@PathVariable("userId") Long userId) {
        userDomainService.lockAccount(userId);
        return JsonResult.success("用户禁用成功");
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/unlock")
    @Operation(summary = "解锁用户", description = "解锁被锁定的用户")
    @PreAuthorize("hasAuthority('user:unlock')")
    public JsonResult<String> unlockUser(@PathVariable("userId") Long userId) {
        userDomainService.unLockAccount(userId);
        return JsonResult.success();
    }

    /**
     * 给用户添加角色
     *
     */
    @PostMapping("/assignRoleToUser")
    @Operation(summary = "为用户指定角色")
    @PreAuthorize("hasAuthority('user:assign-role')")
    public JsonResult<Void> assignRoleToUser(@RequestBody AssignRoleRequest request) {
        userRoleAssignmentService.assignRole(AssignRoleCommand.create(request.getUserId(), request.getRoleId()));
        return JsonResult.success();
    }

    /**
     * 批量为用户分配角色
     * 保证事务性，分配完成后通过事件踢出用户登录
     *
     * @param request 包含用户ID和角色ID列表
     * @return 操作结果
     */
    @PostMapping("/assignRolesToUser")
    @Operation(summary = "批量为用户分配角色")
    @PreAuthorize("hasAuthority('user:assign-role')")
    @Transactional
    public JsonResult<Void> assignRolesToUser(@RequestBody AssignRolesRequest request) {
        log.info("批量为用户 {} 分配角色: {}", request.getUserId(), request.getRoleIds());

        userRoleAssignmentService.assignRoles(
            AssignRolesCommand.create(request.getUserId(), request.getRoleIds())
        );

        log.info("批量角色分配完成");
        return JsonResult.success();
    }

    /**
     * 撤销用户角色
     *
     */
    @PostMapping("/revokeRoleForUser")
    @Operation(summary = "撤销用户角色")
    @PreAuthorize("hasAuthority('user:revoke-role')")
    public JsonResult<Void> revokeRoleForUser(@RequestBody RevokeRoleRequest request) {
        userRoleAssignmentService.revokeRoleForUser(RevokeRoleCommand.create(request.getUserId(), request.getRoleId()));
        return JsonResult.success();
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

    /**
     * 内部类：分配角色请求
     */
    @lombok.Data
    public static class AssignRoleRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("userId")
        private Long userId;
        @com.fasterxml.jackson.annotation.JsonProperty("roleId")
        private Long roleId;
    }

    /**
     * 内部类：批量分配角色请求
     */
    @lombok.Data
    public static class AssignRolesRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("userId")
        private Long userId;
        @com.fasterxml.jackson.annotation.JsonProperty("roleIds")
        private List<Long> roleIds;
    }

    /**
     * 内部类：撤销角色请求
     */
    @lombok.Data
    public static class RevokeRoleRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("userId")
        private Long userId;
        @com.fasterxml.jackson.annotation.JsonProperty("roleId")
        private Long roleId;
    }
}
