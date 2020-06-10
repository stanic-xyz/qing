package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private ZhangliProperties zhangliProperties = null;

    public TokenProvider(ZhangliProperties properties) {

        this.zhangliProperties = properties;
    }

    public String getUserNameFromToken(String authToken) {
        try {
            Claims claims = JwtUtil.parseJWT(authToken, zhangliProperties.getSecurity().getSecretKey());
            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateToken(String authToken, UserDetails details) {
        return true;
    }
}
