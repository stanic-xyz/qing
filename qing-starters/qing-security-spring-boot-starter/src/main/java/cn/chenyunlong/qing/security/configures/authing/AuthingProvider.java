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

package cn.chenyunlong.qing.security.configures.authing;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.authing.sdk.java.dto.authentication.UserInfo;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.qing.security.entity.AuthToken;
import cn.chenyunlong.qing.security.entity.AuthUser;
import cn.chenyunlong.qing.security.entity.ConnectionData;
import cn.chenyunlong.qing.security.enums.AuthProvider;
import cn.chenyunlong.qing.security.exception.NotConnectedException;
import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
import cn.chenyunlong.qing.security.signup.ConnectionService;
import cn.chenyunlong.qing.security.token.Auth2AuthenticationToken;
import cn.chenyunlong.qing.security.userdetails.TemporaryUser;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@RequiredArgsConstructor
public class AuthingProvider implements AuthenticationProvider {

    private final AuthingProperties authingProperties;
    private final UmsUserDetailsService userDetailsService;
    private final ConnectionService connectionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AuthingLoginToken loginToken = (AuthingLoginToken) authentication;
        //1、从第三方获取 Userinfo
        AuthingLoginRequest loginRequest = loginToken.getLoginRequest();
        log.info("【Authing】通过Authing登录：流程开始");
        log.info("【Authing】code:{},state:{}", loginRequest.getCode(), loginRequest.getState());
        // 1.1设置初始化参数
        AuthenticationClient authenticationClient = getAuthenticationClient();
        //1.3 通过code 获取OIDCTokenResponse
        OIDCTokenResponse accessToken;
        try {
            accessToken = authenticationClient.getAccessTokenByCode(loginRequest.getCode());
        } catch (Exception exception) {
            throw new AuthenticationCredentialsNotFoundException("Authing认证失败：" + exception.getMessage());
        }
        log.info("【Authing】通过Authing登录成功：accessToken:{}", accessToken.getAccessToken());
        UserInfo userInfo = authenticationClient.getUserInfoByAccessToken(accessToken.getAccessToken());
        log.info("【Authing】通过Authing登录成功：userInfo:{}", JSONUtil.toJsonPrettyStr(userInfo));
        String name = userInfo.getName();
        log.info("【Authing】通过Authing登录成功：authing用户名:{}，sub：{}", name, userInfo.getSub());

        //2 查询是否已经有第三方的授权记录, List 按 rank 排序, 直接取第一条记录
        //3 获取 securityContext 中的 authenticationToken, 判断是否为本地登录用户(不含匿名用户)

        //4.1 没有第三方登录记录, 自动注册 或 绑定 或 临时创建第三方登录用户
        AuthProvider provider = AuthProvider.AUTHING;
        List<ConnectionData> connectionDataList = connectionService.findConnectionByProviderIdAndProviderUserId(provider, userInfo.getSub());
        // 没有关联的用户并且开启了用户自动注册功能
        UserDetails userDetails;
        if (CollectionUtil.isNotEmpty(connectionDataList)) {
            log.info("【Authing】通过Authing登录成功：关联到用户信息:{}", connectionDataList.stream().map(ConnectionData::getUserId).collect(Collectors.joining(",")));
            ConnectionData connectionData = connectionDataList.stream()
                                                .min(Comparator.comparing(ConnectionData::getRank))
                                                .orElseThrow(() -> new IllegalArgumentException("未找到匹配的用户信息"));
            userDetails = userDetailsService.loadUserByUserId(connectionData.getUserId());
        } else {
            log.info("【Authing】通过Authing登录成功：未关联到用户信息, 开始自动注册");
            // 自动注册
            AuthUser authUser;
            authUser = AuthUser.builder()
                           .username(userInfo.getName())
                           .uuid(userInfo.getSub())
                           .avatar(userInfo.getPicture())
                           .source(provider)
                           .token(AuthToken.builder()
                                      .accessCode(accessToken.getAccessToken())
                                      .expireIn(accessToken.getExpiresIn().longValue())
                                      .code(loginRequest.getCode())
                                      .userId(userInfo.getSub())
                                      .idToken(accessToken.getIdToken())
                                      .openId(userInfo.getSub())
                                      .refreshToken(accessToken.getRefreshToken())
                                      .build())
                           .build();
            // 启用自动注册，进入自动注册流程
            if (authingProperties.getAutoSignUp()) {
                log.info("【Authing】通过Authing登录成功：启用自动注册，进入自动注册流程");
                userDetails = connectionService.signUp(authUser, loginRequest.getState());
                log.info("【Authing】通过Authing登录成功：自动注册成功, 用户信息:{}", userDetails.getUsername());
            } else {
                // 未启用自动注册，返回临时用户
                log.info("【Authing】通过Authing登录成功：未启用自动注册，返回临时用户");
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authingProperties.getDefaultAuthorities());
                userDetails = TemporaryUser.builder()
                                  .username(authUser.getUsername())
                                  .password("123456")
                                  .authorities(grantedAuthority)
                                  .encodeState(loginRequest.getState())
                                  .authUser(authUser)
                                  .build();
            }
        }
        //4.2 第三方登录用户已存在, 直接登录
        if (userDetails == null) {
            log.info("【Authing】通过Authing登录成功：但是未找到匹配的用户信息");
            throw new NotConnectedException(provider.getProviderId());
        }
        // 认证成功
        authentication.setAuthenticated(true);
        // 7 创建成功认证 token 并返回
        Auth2AuthenticationToken auth2AuthenticationToken = new Auth2AuthenticationToken(userDetails, userDetails.getAuthorities(), provider.getProviderId());
        auth2AuthenticationToken.setDetails(loginToken.getDetails());
        return auth2AuthenticationToken;
    }

    private AuthenticationClient getAuthenticationClient() {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingProperties.getAppId());
        clientOptions.setAppSecret(authingProperties.getAppSecret());
        clientOptions.setAppHost(authingProperties.getAppHost());
        clientOptions.setRedirectUri(authingProperties.getRedirectUrl());
        // 1.2初始化 AuthenticationClient
        AuthenticationClient authenticationClient;
        try {
            authenticationClient = new AuthenticationClient(clientOptions);
        } catch (IOException e) {
            throw new AuthenticationCredentialsNotFoundException("读取配置文件异常");
        } catch (ParseException exception) {
            throw new AuthenticationCredentialsNotFoundException("解析Authing配置错误");
        }
        return authenticationClient;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthingLoginToken.class.isAssignableFrom(authentication);
    }
}
