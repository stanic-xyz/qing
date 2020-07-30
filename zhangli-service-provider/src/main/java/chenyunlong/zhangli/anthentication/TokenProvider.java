package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * @param jwtToken
     * @return
     */
    public User getUserNameFromToken(String jwtToken) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtToken, zhangliProperties.getSecurity().getSecretKey());
            return objectMapper.readValue(claims.getSubject(), User.class);
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

    /**
     * 酱token生成jwt生成token信息
     *
     * @param authentication token
     * @param rememberMe     是否记住我，认证信息时间设置为一个月
     * @return
     * @throws JsonProcessingException 解析json信息错误
     */
    public String createToken(Authentication authentication, boolean rememberMe) throws JsonProcessingException {
        String username = (String) authentication.getPrincipal();
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
                .setSubject(username)
                .claim(zhangliProperties.getSecurity().getAuthticationPrefix(), authorities)
                .signWith(SignatureAlgorithm.HS512, zhangliProperties.getSecurity().getSecretKey())
                .setExpiration(validity)
                .compact();
    }

    /**
     * 通过jwt获取token信息
     *
     * @param jwt jwt认证信息
     * @return
     * @throws JsonProcessingException
     */
    public Authentication getAuthentication(String jwt) throws JsonProcessingException {
        Claims claims = Jwts.parser()
                .setSigningKey(zhangliProperties.getSecurity().getSecretKey())
                .parseClaimsJws(jwt)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(zhangliProperties.getSecurity().getAuthticationPrefix()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    /**
     * 验证认证信息是否有效
     *
     * @param jwtToken jwtToken
     * @return
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(zhangliProperties.getSecurity().getSecretKey()).parseClaimsJws(jwtToken);
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
