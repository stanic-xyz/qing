/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.web.web.system;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.GetProfileDto;
import cn.authing.sdk.java.dto.UserDto;
import cn.authing.sdk.java.dto.UserSingleRespDto;
import cn.authing.sdk.java.dto.authentication.IOidcParams;
import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

/**
 * 对接微信公众平台
 *
 * @author 陈云龙
 * @since 2022/11/05
 */
@Slf4j
@Tag(name = "微信公众平台对接")
@RestController
@RequestMapping("api/authorize/authing")
@RequiredArgsConstructor
public class AuthingLoginController {

    private final AuthingProperties authingProperties;

    /**
     * 跳转到Authing登录界面。
     */
    @GetMapping("login")
    public void login(HttpServletResponse response) throws IOException, ParseException {
        // 设置初始化参数
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingProperties.getAppId());
        clientOptions.setAppSecret(authingProperties.getAppSecret());
        clientOptions.setAppHost(authingProperties.getAppHost());
        clientOptions.setRedirectUri(authingProperties.getRedirectUrl());

        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);
        IOidcParams params = new IOidcParams();
        response.sendRedirect(authenticationClient.buildAuthorizeUrl(params));
    }

    /**
     * 跳转到Authing登录界面。
     */
    @GetMapping("accessToken")
    public JsonResult<OIDCTokenResponse> accessToken(String code, String state) throws Exception {
        // 设置初始化参数
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingProperties.getAppId());
        clientOptions.setAppSecret(authingProperties.getAppSecret());
        clientOptions.setAppHost(authingProperties.getAppHost());
        clientOptions.setRedirectUri(authingProperties.getRedirectUrl());

        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);
        return JsonResult.success(authenticationClient.getAccessTokenByCode(code));
    }

    /**
     * 跳转到Authing登录界面。
     */
    @GetMapping("getProfile")
    public JsonResult<UserDto> getProfile(String accessToken) throws Exception {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingProperties.getAppId());
        clientOptions.setAppSecret(authingProperties.getAppSecret());
        clientOptions.setAppHost(authingProperties.getAppHost());
        clientOptions.setRedirectUri(authingProperties.getRedirectUrl());
        clientOptions.setAccessToken(accessToken);

        // 初始化 AuthenticationClient
        AuthenticationClient authenticationClient = new AuthenticationClient(clientOptions);
        GetProfileDto reqDto = new GetProfileDto();
        reqDto.setWithCustomData(true);
        reqDto.setWithIdentities(true);
        reqDto.setWithDepartmentIds(true);
        UserSingleRespDto profile = authenticationClient.getProfile(reqDto);
        if (profile.getStatusCode() != 200) {
            throw new BusinessException(profile.getMessage());
        }
        return JsonResult.success(profile.getData());
    }

    /**
     * 跳转到Authing登录界面。
     */
    @GetMapping("getVersion")
    public JsonResult<String> getProfile() {
        return JsonResult.success("0.0.2-SNAPSHOT");
    }
}
