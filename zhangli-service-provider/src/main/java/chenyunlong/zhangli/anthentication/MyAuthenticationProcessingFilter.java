package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;


    public MyAuthenticationProcessingFilter(ZhangliProperties zhangliProperties, RedisTemplate redisTemplate) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.zhangliProperties = zhangliProperties;
        this.redisTemplate = redisTemplate;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);

        if (!StringUtils.isBlank(token))
            return token;
        token = request.getParameter(TOKEN);
        if (!StringUtils.isBlank(token))
            return token;
        if (!StringUtils.isBlank(token))
            return token;
        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        UsernamePasswordAuthenticationToken authRequest;


//        authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);


        logger.debug("在这里debug");
        return null;
    }
}
