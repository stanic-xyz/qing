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

package cn.chenyunlong.security.config.security.configures.my;

import cn.chenyunlong.common.model.ApiResult;
import cn.chenyunlong.security.config.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 登录成功处理器。
 *
 * @author 陈云龙
 */
@Slf4j
@Component
@AllArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String username = (String) authentication.getPrincipal();
        //构建一个Token
        JwtBuilder builder = Jwts.builder();
        //设置主体信息
        String token = builder.setSubject(username)
            //设置过期时间
            .setExpiration(
                new Date(System.currentTimeMillis() + securityProperties.getJwtTimeOut()))
            .setId(authentication.getPrincipal().toString())
            .signWith(SignatureAlgorithm.HS512, securityProperties.getSecretKey())
            .compact();
        ApiResult<String> success = ApiResult.success(token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(success));
    }
}
