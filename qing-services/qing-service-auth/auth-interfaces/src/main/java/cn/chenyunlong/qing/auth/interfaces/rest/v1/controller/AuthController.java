package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.UserService;
import cn.chenyunlong.qing.auth.application.dto.AuthenticationResult;
import cn.chenyunlong.qing.auth.application.dto.LoginRequest;
import cn.chenyunlong.qing.auth.application.dto.LoginResponse;
import cn.chenyunlong.qing.auth.application.service.AuthenticationService;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * 认证控制器
 * 提供用户注册、登录等功能
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证接口", description = "提供用户注册、登录等功能")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户")
    public JsonResult<String> register(@RequestBody RegisterRequest request) {
        UserCreator creator = new UserCreator();
        creator.setUsername(request.getUsername());
        creator.setPassword(request.getPassword());
        creator.setEmail(request.getEmail());

        Optional<QingUser> userOptional = userService.register(creator);
        if (userOptional.isPresent()) {
            return JsonResult.success("注册成功");
        } else {
            return JsonResult.fail("注册失败");
        }
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
    public JsonResult<LoginResponse> login(
        @RequestBody cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.LoginRequest request,
        HttpServletRequest httpServletRequest) {

        // 转换为应用层DTO
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());

        // 获取客户端信息
        String ipAddress = getClientIpAddress(httpServletRequest);
        String userAgent = httpServletRequest.getHeader("User-Agent");

        // 调用应用服务进行认证
        AuthenticationResult result =
            authenticationService.login(loginRequest, ipAddress, userAgent);

        // 转换认证结果
        LoginResponse response = LoginResponse.fromAuthenticationResult(result);
        if (result.isSuccess()) {
            // 转换为接口层DTO
            LoginResponse interfaceResponse = new LoginResponse();
            interfaceResponse.setTokenInfo(result.getTokenInfo());
            interfaceResponse.setUserId(result.getUser().getId().getValue());
            interfaceResponse.setUsername(result.getUser().getUsername());
            return JsonResult.success(interfaceResponse);
        } else {
            return JsonResult.fail(result.getFailureReason());
        }
    }

    /**
     * 获取当前用户信息
     *
     * @param token              认证token
     * @param httpServletRequest HTTP请求
     * @return 用户信息
     */
    @GetMapping("/current-user")
    @Operation(summary = "获取当前用户信息", description = "根据token获取当前登录用户信息")
    public JsonResult<QingUser> getCurrentUser(
        @RequestHeader("Authorization") String token,
        HttpServletRequest httpServletRequest) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 获取客户端信息
            String ipAddress = getClientIpAddress(httpServletRequest);
            String userAgent = httpServletRequest.getHeader("User-Agent");

            // 验证令牌
            AuthenticationResult result = authenticationService.validateToken(token, ipAddress, userAgent);

            if (result.isSuccess()) {
                return JsonResult.success(result.getUser());
            } else {
                return JsonResult.fail(result.getFailureReason());
            }
        } catch (Exception e) {
            log.error("解析token失败", e);
            return JsonResult.fail("无效的token");
        }
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
            authenticationService.logout(token);

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
