package chenyunlong.zhangli.anthentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends GenericFilterBean {

    private static final String TOKEN = "Authorization";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String jwtToken = req.getHeader(TOKEN);
//        log.debug(TOKEN + ":" + jwtToken);
        if (jwtToken != null) {
            try {
                Claims claims = Jwts.parser().setSigningKey("sang@123").parseClaimsJws(jwtToken.replace("Bearer", ""))
                        .getBody();
                String username = claims.getSubject();//获取当前登录用户名
            } catch (Exception exp) {
                System.out.println("token无效！");
            }
        }
        filterChain.doFilter(req, servletResponse);
    }
}