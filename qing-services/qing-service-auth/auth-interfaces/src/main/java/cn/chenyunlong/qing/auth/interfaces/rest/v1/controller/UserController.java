package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.application.service.UserRoleAssignmentService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.AdminActiveUserCommand;
import cn.chenyunlong.qing.auth.domain.user.command.AdminDeActiveUserCommand;
import cn.chenyunlong.qing.auth.domain.user.command.AssignRoleCommand;
import cn.chenyunlong.qing.auth.domain.user.command.RevokeRoleCommand;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.AssignRoleRequest;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role.RevokeRoleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public JsonResult<User> getUserById(@PathVariable("userId") Long userId) {
        Optional<User> userOptional = userDomainService.loadUserById(userId);
        return userOptional.map(JsonResult::success)
                .orElseGet(() -> JsonResult.fail("用户不存在"));
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "激活指定用户")
    //    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<String> activateUser(@PathVariable("userId") Long userId) {
        userDomainService.activeFromAdmin(AdminActiveUserCommand.create(userId, "admin"));
        return JsonResult.success("用户激活成功");
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/deActivate")
    @Operation(summary = "激活用户", description = "激活指定用户")
    //    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<String> deActivate(@PathVariable("userId") Long userId) {
        userDomainService.deActiveFromAdmin(AdminDeActiveUserCommand.create(userId, "admin"));
        return JsonResult.success("用户激活成功");
    }

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PatchMapping("/{userId}/lock")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    //    @PreAuthorize("hasRole('ADMIN')")
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
    //    @PreAuthorize("hasRole('ADMIN')")
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
    //    @PreAuthorize("hasRole('ADMIN')")
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
    //    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<Void> revokeRoleForUser(@RequestBody RevokeRoleRequest request) {
        userRoleAssignmentService.revokeRoleForUser(RevokeRoleCommand.create(request.getUserId(), request.getRoleId()));
        return JsonResult.success();
    }
}
