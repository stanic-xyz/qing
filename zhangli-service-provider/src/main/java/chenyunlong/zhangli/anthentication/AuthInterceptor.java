package chenyunlong.zhangli.anthentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthInterceptor extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private static final String TOKEN = "Authorization";
    private final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getToken(request);

        if (!StringUtils.isBlank(jwtToken)) {
            try {
                Claims claims = Jwts.parser().setSigningKey("sang@123").parseClaimsJws(jwtToken.replace("Bearer", ""))
                        .getBody();
                String username = claims.getSubject();//获取当前登录用户名
            } catch (Exception exp) {
                System.out.println("token无效！");
            }
        }
        filterChain.doFilter(request, response);
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
}
