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

package cn.chenyunlong.security.configures.authing;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.authing.sdk.java.dto.authentication.UserInfo;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.security.entity.AuthUser;
import cn.chenyunlong.security.entity.ConnectionData;
import cn.chenyunlong.security.signup.ConnectionService;
import cn.chenyunlong.security.token.Auth2AuthenticationToken;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthingProvider implements AuthenticationProvider {

    private final AuthingProperties authing;
    private final UserDetailsService userDetailsService;
    private final ConnectionService connectionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthingLoginToken loginToken = (AuthingLoginToken) authentication;

        //1、从第三方获取 Userinfo
        AuthingLoginRequest loginRequest = loginToken.getLoginRequest();
        log.info("【Authing】通过Authing登录：流程开始");
        log.info("【Authing】code:{},state:{}", loginRequest.getCode(), loginRequest.getState());
        authentication.setAuthenticated(true);
        // 1.1设置初始化参数
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authing.getAppId());
        clientOptions.setAppSecret(authing.getAppSecret());
        clientOptions.setAppHost(authing.getAppHost());
        clientOptions.setRedirectUri(authing.getRedirectUrl());
        // 1.2初始化 AuthenticationClient
        AuthenticationClient authenticationClient;
        try {
            authenticationClient = new AuthenticationClient(clientOptions);
            //1.3 通过code 获取OIDCTokenResponse
            OIDCTokenResponse accessTokenByCode = authenticationClient.getAccessTokenByCode(loginRequest.getCode());
            log.info("【Authing】通过Authing登录成功：accessToken:{}", accessTokenByCode.getAccessToken());
            UserInfo userInfo = authenticationClient.getUserInfoByAccessToken(accessTokenByCode.getAccessToken());
            log.info("【Authing】通过Authing登录成功：userInfo:{}", JSONUtil.toJsonPrettyStr(userInfo));
            String name = userInfo.getName();
            log.info("【Authing】通过Authing登录成功：authing用户名:{}，sub：{}", name, userInfo.getSub());

            //2 查询是否已经有第三方的授权记录, List 按 rank 排序, 直接取第一条记录
            //3 获取 securityContext 中的 authenticationToken, 判断是否为本地登录用户(不含匿名用户)

            UserDetails userDetails = null;
            //4.1 没有第三方登录记录, 自动注册 或 绑定 或 临时创建第三方登录用户
            String providerId = "authing";
            List<ConnectionData> connectionDataList = connectionService.findConnectionByProviderIdAndProviderUserId(providerId, userInfo.getSub());
            if (CollectionUtil.isEmpty(connectionDataList)) {
                // 自动注册// 自动注册
                userDetails = connectionService.signUp(AuthUser.builder().build(), providerId, loginRequest.getState());
            }
            //4.2 第三方登录用户已存在, 直接登录
            if (userDetails == null) {
            }
            // 7 创建成功认证 token 并返回
            Auth2AuthenticationToken auth2AuthenticationToken = new Auth2AuthenticationToken(userDetails, userDetails.getAuthorities(), providerId);
            auth2AuthenticationToken.setDetails(loginToken.getDetails());
            return authentication;
        } catch (Exception exception) {
            throw new AuthenticationCredentialsNotFoundException("Authing配置错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthingLoginToken.class.isAssignableFrom(authentication);
    }
}
