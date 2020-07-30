package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final Logger logger = LoggerFactory.getLogger(MyAuthenticationProcessingFilter.class);
    private static final String TOKEN = "Authorization";

    private final ZhangliProperties zhangliProperties;


    public MyAuthenticationProcessingFilter(ZhangliProperties zhangliProperties) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.zhangliProperties = zhangliProperties;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        UsernamePasswordAuthenticationToken authRequest;
        authRequest = new UsernamePasswordAuthenticationToken("", "", null);

        logger.debug(zhangliProperties.getSecurity().getJwtTimeOut().toString());
        logger.debug("在这里debug");
        return this.getAuthenticationManager().authenticate(authRequest);
    }


}
