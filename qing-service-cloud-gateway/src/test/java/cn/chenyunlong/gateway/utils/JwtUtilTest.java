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
