package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.application.service.UserRoleAssignmentService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.*;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.AssignRoleRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.RevokeRoleRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.user.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<User> getUserById(@PathVariable("id") Long userId) {
        Optional<User> userOptional = userDomainService.loadUserById(userId);
        return userOptional.map(JsonResult::success)
                .orElseGet(() -> JsonResult.fail("用户不存在"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑用户信息")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<String> updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserRequest request) {
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
    @PreAuthorize("hasAuthority('user:active')")
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
    @PreAuthorize("hasAuthority('user:deactive')")
    public JsonResult<String> deActivate(@PathVariable("userId") Long userId) {
        userDomainService.deActiveFromAdmin(AdminDeActiveUserCommand.create(userId, "admin"));
        return JsonResult.success("用户禁用成功");
    }

    /**
     * 管理员创建用户
     * TODO: 实现管理员创建用户逻辑
     */
    @PostMapping
    @Operation(summary = "管理员创建用户")
    @PreAuthorize("hasAuthority('user:create')")
    public JsonResult<Void> createUser(@RequestBody @Valid Object request) {
        // TODO: 实现创建用户逻辑
        return JsonResult.success();
    }

    /**
     * 删除用户
     * TODO: 实现删除用户逻辑
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public JsonResult<Void> deleteUser(@PathVariable("id") Long userId) {
        // TODO: 实现删除用户逻辑
        return JsonResult.success();
    }

    /**
     * 获取用户列表
     * TODO: 实现分页查询用户列表
     */
    @GetMapping
    @Operation(summary = "获取用户列表")
    @PreAuthorize("hasAuthority('user:list')")
    public JsonResult<Object> listUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        // TODO: 实现分页查询逻辑
        return JsonResult.success(null);
    }

    /**
     * 发送更换邮箱验证码
     * TODO: 实现发送验证码逻辑
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
     * TODO: 实现验证码校验和邮箱更换逻辑
     */
    @PostMapping("/email/change")
    @Operation(summary = "确认更换邮箱")
    @PreAuthorize("isAuthenticated()")
    public JsonResult<Void> changeEmail(@RequestBody Object request) {
        // TODO: 实现更换邮箱逻辑
        return JsonResult.success();
    }


    /**
     * 禁用用户
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
    @PostMapping("assignRoleToUser")
    @Operation(summary = "为用户指定角色")
    @PreAuthorize("hasAuthority('user:assign-role')")
    public JsonResult<Void> assignRoleToUser(@RequestBody AssignRoleRequest request) {
        userRoleAssignmentService.assignRole(AssignRoleCommand.create(request.getUserId(), request.getRoleId()));
        return JsonResult.success();
    }

    /**
     * 给用户添加角色
     *
     */
    @PostMapping("revokeRoleForUser")
    @Operation(summary = "激活用户", description = "激活指定用户")
    @PreAuthorize("hasAuthority('user:revoke-role')")
    public JsonResult<Void> revokeRoleForUser(@RequestBody RevokeRoleRequest request) {
        userRoleAssignmentService.revokeRoleForUser(RevokeRoleCommand.create(request.getUserId(), request.getRoleId()));
        return JsonResult.success();
    }
}
