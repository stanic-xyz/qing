package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.UserService;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final UserService userService;

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public JsonResult<QingUser> getUserById(@PathVariable Long userId) {
        Optional<QingUser> userOptional = userService.loadUserById(userId);
        return userOptional.map(JsonResult::success)
                .orElseGet(() -> JsonResult.fail("用户不存在"));
    }

    /**
     * 激活用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{userId}/activate")
    @Operation(summary = "激活用户", description = "激活指定用户")
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<String> activateUser(@PathVariable Long userId) {
        userService.validUser(userId);
        return JsonResult.success("用户激活成功");
    }

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{userId}/deactivate")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<String> deactivateUser(@PathVariable Long userId) {
        userService.invalidUser(userId);
        return JsonResult.success("用户禁用成功");
    }

    /**
     * 解锁用户
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{userId}/unlock")
    @Operation(summary = "解锁用户", description = "解锁被锁定的用户")
    @PreAuthorize("hasRole('ADMIN')")
    public JsonResult<String> unlockUser(@PathVariable Long userId) {
        Optional<QingUser> userOptional = userService.loadUserById(userId);
        if (userOptional.isEmpty()) {
            return JsonResult.fail("用户不存在");
        }
        
        QingUser user = userOptional.get();
        try {
            user.unlock();
            return JsonResult.success("用户解锁成功");
        } catch (IllegalStateException e) {
            return JsonResult.fail(e.getMessage());
        }
    }
}
