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

package cn.chenyunlong.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    @Test
    void createJWT() {
        //当前时间
        String jwt = JwtUtil.createJWT("admin", "stanic", JwtUtil.JWT_TTL);
        //构建 并返回一个字符串
        System.out.println(jwt);
        Assertions.assertNotNull(jwt);
    }

    @Disabled
    @Test
    void parseJWT() {
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1NTc5MDU4MDIsImV4cCI6MTU1NzkwNjgwMiwicm9sZXMiOiJhZG1pbiJ9.AS5Y2fNCwUzQQxXh_QQWMpaB75YqfuK-2P7VZiCXEJI";
        Claims claims = Jwts.parser().setSigningKey("itcast").parseClaimsJws(compactJwt).getBody();
        System.out.println(claims);
    }
}
