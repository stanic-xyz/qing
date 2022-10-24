/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.controller.api.auth;

import cn.chenyunlong.qing.annotation.Log;
import cn.chenyunlong.qing.cache.lock.CacheLock;
import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.core.enums.MFAType;
import cn.chenyunlong.qing.core.exception.AbstractException;
import cn.chenyunlong.qing.core.exception.LoginErrorException;
import cn.chenyunlong.qing.model.dto.LoginPreCheckDTO;
import cn.chenyunlong.qing.model.entities.Permission;
import cn.chenyunlong.qing.model.entities.User;
import cn.chenyunlong.qing.model.params.LoginParam;
import cn.chenyunlong.qing.model.params.UserParam;
import cn.chenyunlong.qing.model.vo.system.UserInfoVO;
import cn.chenyunlong.qing.security.support.TokenProvider;
import cn.chenyunlong.qing.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Stan
 */
@Api(tags = "认证授权")
@RestController
@RequestMapping("authorize")
public class AuthController {

    private final AuthGithubRequest authRequest;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public AuthController(AuthGithubRequest authRequest, UserService userService, TokenProvider tokenProvider) {
        this.authRequest = authRequest;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("login/preCheck")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false, prefix = "login_pre_check")
    public LoginPreCheckDTO authPreCheck(@RequestBody @Valid LoginParam loginParam) {
        final User user = userService.authenticate(loginParam);
        return new LoginPreCheckDTO(MFAType.useMFA(user.getMfaType()));
    }

    @Log(title = "通过表单登陆")
    @ApiOperation("通过表单登陆")
    @PostMapping(value = "formLogin")
    public ApiResult<UserInfoVO> formLoin(@RequestBody LoginParam loginParam) throws LoginErrorException {
        User authenticate = userService.authenticate(loginParam);
        UserInfoVO userInfoVO = new UserInfoVO().convertFrom(authenticate);
        String jwtToken = tokenProvider.createJwtToken(userInfoVO, false);
        userInfoVO.setToken(jwtToken);
        return ApiResult.success(userInfoVO);
    }

    @PostMapping("register")
    public ApiResult<User> register(@RequestBody UserParam userParam) throws AbstractException {
        try {
            User user = userService.addUserInfo(userParam.convertTo());
            return ApiResult.success(user);
        } catch (AbstractException exp) {
            return ApiResult.fail(exp.getMessage());
        }
    }

    /**
     * 跳转到登陆表单
     */
    @GetMapping("login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("callback")
    public ApiResult<AuthResponse> accessToken(AuthCallback callback) {
        AuthResponse authResponse = authRequest.login(callback);
        if (authResponse.ok()) {
            AuthUser authUser = (AuthUser) authResponse.getData();
            User user = userService.getUserInfoByThird(authUser);
            if (user == null) {
                return ApiResult.fail("未绑定！");
            }

            List<Permission> permissionList = userService.getPermissionByUsername(user.getUsername());
            List<GrantedAuthority> authorities = new LinkedList<>();
            permissionList.stream().map(permission ->
                            new SimpleGrantedAuthority("ROLE_" + permission.getName()))
                    .forEach(authorities::add);
            ApiResult.success(tokenProvider.createJwtToken(new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getToken(), authorities), false));
        }
        return ApiResult.success(authResponse);
    }
}