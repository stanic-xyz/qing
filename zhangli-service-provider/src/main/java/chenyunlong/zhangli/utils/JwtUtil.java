package chenyunlong.zhangli.utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {


    public static String createJWT(String jwtId, String body, String secretKey, long timeOut) {
        JwtBuilder builder = Jwts.builder()
                .setId(jwtId)                                      // JWT_ID
                .setAudience("")                                // 接受者
                .setClaims(null)                                // 自定义属性
                .setSubject(body)                                 // 主题
                .setIssuer("")                                  // 签发者
                .setIssuedAt(new Date())                        // 签发时间
                .setNotBefore(new Date())                       // 失效时间
                .setExpiration(new Date(System.currentTimeMillis() + timeOut))                      // 过期时间
                .signWith(SignatureAlgorithm.HS512, secretKey);           // 签名算法以及密匙
        return builder.compact();
    }

    public static Claims parseJWT(String authToken, String jwtSecretKey) throws JwtException {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(authToken)
                .getBody();
    }
}
