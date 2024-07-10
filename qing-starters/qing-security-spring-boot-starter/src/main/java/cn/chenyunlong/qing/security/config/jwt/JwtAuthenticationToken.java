package cn.chenyunlong.qing.security.config.jwt;

import java.util.Collections;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    private final String userAgent;

    public JwtAuthenticationToken(String token, String userAgent) {
        super(Collections.emptyList());
        this.token = token;
        this.userAgent = userAgent;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
