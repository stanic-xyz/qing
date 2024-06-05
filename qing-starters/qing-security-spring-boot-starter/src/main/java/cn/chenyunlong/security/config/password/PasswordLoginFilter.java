package cn.chenyunlong.security.config.password;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class PasswordLoginFilter extends AbstractAuthenticationProcessingFilter {

    public PasswordLoginFilter() {
        super(new AntPathRequestMatcher("/api/auth/passLogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PasswordLoginRequest loginRequest =
            mapper.readValue(request.getReader(), PasswordLoginRequest.class);
        return new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword());
    }
}
