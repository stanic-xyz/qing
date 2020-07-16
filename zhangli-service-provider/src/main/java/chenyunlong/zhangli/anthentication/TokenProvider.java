package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final ZhangliProperties zhangliProperties;
    private final ObjectMapper objectMapper;

    public TokenProvider(ZhangliProperties zhangliProperties, ObjectMapper objectMapper) {
        this.zhangliProperties = zhangliProperties;
        this.objectMapper = objectMapper;
    }


    /**
     * 通过token获取用户信息
     *
     * @param authToken
     * @return
     */
    public String getUserNameFromToken(String authToken) {
        try {
            Claims claims = JwtUtil.parseJWT(authToken, zhangliProperties.getSecurity().getSecretKey());
            User userInfo = objectMapper.readValue(claims.getSubject(), User.class);
            return userInfo.getUsername();
        } catch (SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
        } catch (JsonProcessingException e) {
            logger.info("parse Jsonobject failed.");
        } catch (Exception e) {
            logger.error("解析jwt token 错误", e);
        }
        return null;
    }

    public String createToken(Authentication authentication, boolean rememberMe) throws JsonProcessingException {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + zhangliProperties.getSecurity().getTokenValidityInMilliseconds());
        } else {
            validity = new Date(now + zhangliProperties.getSecurity().getTokenValidityInMillisecondsForRememberMe());
        }

        return Jwts.builder()
                .setSubject(new ObjectMapper().writeValueAsString(authentication))
                .claim(zhangliProperties.getSecurity().getAuthticationPrefix(), authorities)
                .signWith(SignatureAlgorithm.HS512, zhangliProperties.getSecurity().getSecretKey())
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) throws JsonProcessingException {
        Claims claims = Jwts.parser()
                .setSigningKey(zhangliProperties.getSecurity().getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(zhangliProperties.getSecurity().getAuthticationPrefix()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new ObjectMapper().readValue(claims.getSubject(), UsernamePasswordAuthenticationToken.class);

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(zhangliProperties.getSecurity().getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
        }
        return false;
    }
}
