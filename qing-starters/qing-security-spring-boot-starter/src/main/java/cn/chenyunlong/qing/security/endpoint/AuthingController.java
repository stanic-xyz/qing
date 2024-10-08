/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.qing.security.endpoint;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.authentication.IOidcParams;
import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.authing.sdk.java.dto.authentication.UserInfo;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.qing.security.endpoint.model.AuthingLoginParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/authorize/auth")
@RequiredArgsConstructor
public class AuthingController {
    private final AuthingProperties authing;


    /**
     * 登录接口。
     */
    @Operation(summary = "跳转到登录地址")
    @GetMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response) {
        try {
            AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
            clientOptions.setAppId(authing.getAppId()); // Authing 应用 ID
            clientOptions.setAppSecret(authing.getAppSecret()); // Authing 应用密钥
            clientOptions.setAppHost(
                authing.getAppHost()); // Authing 应用域名，如 https://example.authing.cn。注意：Host
            // 地址为示例样式，不同版本用户池的应用 Host 地址形式有所差异，实际地址以 自建应用->应用配置->认证配置 下 `认证地址 `字段为准。
            clientOptions.setRedirectUri(authing.getRedirectUrl()); // Authing 应用配置的登录回调地址
            // 初始化 AuthenticationClient
            AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);

            // 生成用于登录的一次性地址，之后可以引导用户访问此地址
            IOidcParams params = new IOidcParams();
            params.setRedirectUri(authing.getRedirectUrl());
            response.sendRedirect(authenticationClient.buildAuthorizeUrl(params));
            return ResponseEntity.ok().build();
        } catch (IOException | ParseException exception) {
            log.error("跳转登录地址发生了异常", exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 通过 code 换取 AccessToken。
     */
    @Operation(summary = "通过code换取AccessToken")
    @PostMapping("/accessToken")
    public ResponseEntity<UserInfo> getUserInfo(@Validated @RequestBody AuthingLoginParam param,
                                                HttpServletResponse response) throws Exception {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authing.getAppId()); // Authing 应用 ID
        clientOptions.setAppSecret(authing.getAppSecret()); // Authing 应用密钥
        clientOptions.setAppHost(
            authing.getAppHost()); // Authing 应用域名，如 https://example.authing.cn。注意：Host
        // 地址为示例样式，不同版本用户池的应用 Host 地址形式有所差异，实际地址以 自建应用->应用配置->认证配置 下 `认证地址 `字段为准。
        clientOptions.setRedirectUri(authing.getRedirectUrl()); // Authing 应用配置的登录回调地址
        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);

        OIDCTokenResponse accessTokenByCode =
            authenticationClient.getAccessTokenByCode(param.getCode());

        UserInfo userInfoByAccessToken =
            authenticationClient.getUserInfoByAccessToken(accessTokenByCode.getAccessToken());
        response.addCookie(new Cookie("qing-token", accessTokenByCode.getAccessToken()));
        // 生成用于登录的一次性地址，之后可以引导用户访问此地址
        return ResponseEntity.ok().body(userInfoByAccessToken);
    }

    /**
     * 根据 AccessToken获取用户信息。
     *
     * @param accessToken 访问令牌
     */
    @Operation(summary = "根据AccessToken获取用户信息")
    @GetMapping("/getUserInfoByAccessToken")
    public ResponseEntity<UserInfo> getUserInfo(
        @CookieValue(value = "qing-token", required = false) String accessToken) throws Exception {
        // 设置初始化参数
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authing.getAppId()); // Authing 应用 ID
        clientOptions.setAppSecret(authing.getAppSecret()); // Authing 应用密钥
        clientOptions.setAppHost(
            authing.getAppHost()); // Authing 应用域名，如 https://example.authing.cn。注意：Host
        // 地址为示例样式，不同版本用户池的应用 Host 地址形式有所差异，实际地址以 自建应用->应用配置->认证配置 下 `认证地址 `字段为准。
        clientOptions.setRedirectUri(authing.getRedirectUrl()); // Authing 应用配置的登录回调地址
        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);
        UserInfo userInfo = authenticationClient.getUserInfoByAccessToken(accessToken);
        return ResponseEntity.ok().body(userInfo);
    }
}
