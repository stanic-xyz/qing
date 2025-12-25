package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.dto.*;
import cn.chenyunlong.qing.auth.application.dto.dto.UserDTO;
import cn.chenyunlong.qing.auth.application.service.AuthApplicationService;
import cn.chenyunlong.qing.auth.domain.authentication.service.UserDomainService;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.command.AuthenticationByUsernamePasswordCommand;
import cn.chenyunlong.qing.auth.domain.user.command.UserRegistrationCommand;
import cn.chenyunlong.qing.auth.domain.user.command.UserResetActiveCodeCommand;
import cn.chenyunlong.qing.auth.domain.user.dto.response.UserResponse;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.RegisterRequest;
import cn.chenyunlong.qing.auth.interfaces.validation.SecurityValidated;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


/**
 * 认证控制器
 * 提供用户注册、登录等功能
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "认证接口", description = "提供用户注册、登录等功能")
public class AuthController {

    private final AuthApplicationService authApplicationService;
    private final UserDomainService userDomainService;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityValidated
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        UserRegistrationCommand command = UserRegistrationCommand.create(request.getUsername(), request.getPassword(), request.getEmail(), request.getPhone(), request.getNickname());

        User user = userDomainService.register(command);
        return ResponseEntity.created(URI.create("/api/v1/users/" + user.getId().id()))
                .body(UserResponse.from(user));
    }

    /**
     * 用户登录
     *
     * @param request            登录请求
     * @param httpServletRequest HTTP请求
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录认证")
    @SecurityValidated
    public JsonResult<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {

        // 转换为应用层DTO
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());

        // 获取客户端信息
        String ipAddress = getClientIpAddress(httpServletRequest);
        String userAgent = httpServletRequest.getHeader("User-Agent");

        AuthenticationByUsernamePasswordCommand command = AuthenticationByUsernamePasswordCommand.create(loginRequest.getUsername(), loginRequest.getPassword(), ipAddress, userAgent);

        // 调用应用服务进行认证
        AuthenticationResultDTO result = authApplicationService.login(command);

        // 转换认证结果
        if (!result.success()) {
            return JsonResult.fail(result.failureReason());
        }

        // 转换为接口层DTO
        LoginResponse interfaceResponse = LoginResponse.fromAuthenticationResult(result);
        return JsonResult.success(interfaceResponse);
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/active")
    @Operation(summary = "用户登录", description = "用户登录认证")
    @SecurityValidated
    public JsonResult<LoginResponse> active(
            @RequestBody UserActiveRequest request) {
        // 调用应用服务进行认证
        authApplicationService.activateUser(request.getUsername(), request.getActiveCode());
        return JsonResult.success();
    }

    @GetMapping("/action")
    @Operation(summary = "用户登录", description = "用户登录认证")
    @SecurityValidated
    public JsonResult<LoginResponse> action(@RequestParam("action") String action,
                                            @RequestParam("hash") String hash) {

        JSONObject jsonObject = JSONUtil.parseObj(hash);

        String userName = jsonObject.get("userName", String.class);
        String code = jsonObject.get("code", String.class);

        // 调用应用服务进行认证
        authApplicationService.activateUser(userName, code);
        return JsonResult.success();
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/resetActiveCode")
    @Operation(summary = "用户登录", description = "用户登录认证")
    @SecurityValidated
    public JsonResult<LoginResponse> resetActiveCode(
            @RequestBody UserActiveRequest request) {
        // 调用应用服务进行认证
        userDomainService.resetActiveCode(UserResetActiveCodeCommand.create(Username.of(request.getUsername())));
        return JsonResult.success();
    }

    /**
     * 获取当前用户信息
     *
     * @param token 认证token
     * @return 用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "根据token获取当前登录用户信息")
    public JsonResult<UserDTO> getCurrentUser(
            @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 验证令牌
        UserDTO userInfoByToken = authApplicationService.getUserInfoByToken(token);
        return JsonResult.success(userInfoByToken);
    }

    /**
     * 刷新令牌接口
     * 用于处理用户注销登录请求
     *
     * @param request 请求头中的Authorization字段，用于验证用户身份
     * @return 返回操作结果，成功或失败信息
     */
    @PostMapping("/refresh")
    public JsonResult<String> refreshToken(@RequestBody RefreshTokenRequest request) {
        // 注销登录
        String s = authApplicationService.refreshToken(request.getRefreshToken());
        return JsonResult.success(s);
    }

    /**
     * 用户注销
     *
     * @param token 认证token
     * @return 注销结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "注销当前用户登录状态")
    public JsonResult<String> logout(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 注销登录
            authApplicationService.logout(token);

            return JsonResult.success("注销成功");
        } catch (Exception e) {
            log.error("注销失败", e);
            return JsonResult.fail("注销失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
