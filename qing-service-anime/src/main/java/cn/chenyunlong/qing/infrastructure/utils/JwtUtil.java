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

package cn.chenyunlong.qing.infrastructure.utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {


    public static String createJWT(String jwtId, String body, String secretKey, long timeOut) {
        JwtBuilder builder = Jwts.builder()
                .setId(jwtId)
                .setAudience("")
                .setClaims(null)
                .setSubject(body)
                .setIssuer("")
                .setIssuedAt(new Date())
                .setNotBefore(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeOut))
                .signWith(SignatureAlgorithm.HS512, secretKey);
        return builder.compact();
    }

    public static Claims parseJWT(String authToken, String jwtSecretKey) throws JwtException {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(authToken)
                .getBody();
    }
}
