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

package cn.chenyunlong.qing.controller.api.system;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.authentication.IOidcParams;
import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.authing.sdk.java.dto.authentication.UserInfo;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.chenyunlong.qing.infrastructure.config.authing.AuthingConfig;
import cn.chenyunlong.qing.infrastructure.model.params.authing.AuthingLoginParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/authorize/auth")
@RequiredArgsConstructor
public class AuthingController {
    private final AuthingConfig authing;


    @Operation(summary = "跳转到登录地址")
    @GetMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response) {
        try {
            // 设置初始化参数
            AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
            clientOptions.setAppId(authing.getAppId()); // Authing 应用 ID
            clientOptions.setAppSecret(authing.getAppSecret()); // Authing 应用密钥
            clientOptions.setAppHost(authing.getAppHost()); // Authing 应用域名，如 https://example.authing.cn。注意：Host
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

    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    public ResponseEntity<UserInfo> getUserInfo(@Validated @RequestBody AuthingLoginParam param) throws Exception {
        // 设置初始化参数
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authing.getAppId()); // Authing 应用 ID
        clientOptions.setAppSecret(authing.getAppSecret()); // Authing 应用密钥
        clientOptions.setAppHost(authing.getAppHost()); // Authing 应用域名，如 https://example.authing.cn。注意：Host
        // 地址为示例样式，不同版本用户池的应用 Host 地址形式有所差异，实际地址以 自建应用->应用配置->认证配置 下 `认证地址 `字段为准。
        clientOptions.setRedirectUri(authing.getRedirectUrl()); // Authing 应用配置的登录回调地址
        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);

        OIDCTokenResponse accessTokenByCode = authenticationClient.getAccessTokenByCode(param.getCode());

        UserInfo userInfoByAccessToken =
                authenticationClient.getUserInfoByAccessToken(accessTokenByCode.getAccessToken());
        // 生成用于登录的一次性地址，之后可以引导用户访问此地址
        return ResponseEntity.ok().body(userInfoByAccessToken);
    }
}
