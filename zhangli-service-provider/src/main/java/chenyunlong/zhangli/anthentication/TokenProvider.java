package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.JwtUtil;
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

    public TokenProvider(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }


    public String getUserNameFromToken(String authToken) {
        try {
            Claims claims = JwtUtil.parseJWT(authToken, zhangliProperties.getSecurity().getSecretKey());
            return claims.getSubject();
        } catch (Exception e) {
        }
        return null;
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
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
                .setSubject(authentication.getName())
                .claim(zhangliProperties.getSecurity().getAuthticationPrefix(), authorities)
                .signWith(SignatureAlgorithm.HS512, zhangliProperties.getSecurity().getSecretKey())
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(zhangliProperties.getSecurity().getJwtTimeOut().toString())
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(zhangliProperties.getSecurity().getAuthticationPrefix()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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
