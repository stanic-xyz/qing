package chenyunlong.zhangli.common.utils;

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
