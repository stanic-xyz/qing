package chenyunlong.zhangli.security.filter;

import chenyunlong.zhangli.security.support.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyTokenFilter extends GenericFilterBean {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String AUTHORIZATION_QUERY = "token";
    private final TokenProvider tokenProvider;


    public MyTokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt)) {
            // 从自定义tokenProvider中解析用户
            // 这里仍然是调用我们自定义的UserDetailsService，查库，检查用户名是否存在，
            // 如果是伪造的token,可能DB中就找不到username这个人了，抛出异常，认证失败
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                try {
                    Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception exp) {
                    logger.info("token验证错误！");
                }
            }
        }

        // 调用后续的Filter,如果上面的代码逻辑未能复原“session”，SecurityContext中没有想过信息，后面的流程会检测出"需要登录"
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String headerToken = request.getParameter(AUTHORIZATION_QUERY);
        if (StringUtils.hasText(headerToken)) {
            return headerToken;
        }
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
