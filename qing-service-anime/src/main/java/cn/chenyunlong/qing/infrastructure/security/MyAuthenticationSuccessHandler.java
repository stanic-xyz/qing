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

package cn.chenyunlong.qing.infrastructure.security;

import cn.chenyunlong.qing.core.ApiResult;
import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Stan
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final QingProperties qingProperties;

    public MyAuthenticationSuccessHandler(QingProperties qingProperties) {
        this.qingProperties = qingProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String username = (String) authentication.getPrincipal();
        //构建一个Token
        JwtBuilder builder = Jwts.builder();
        //设置主体信息
        String token = builder.setSubject(username)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + qingProperties.getSecurity().getJwtTimeOut()))
                .setId(authentication.getPrincipal().toString())
                .signWith(SignatureAlgorithm.HS512, qingProperties.getSecurity().getSecretKey())
                .compact();
        ApiResult<String> success = ApiResult.success(token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(success));
    }
}
