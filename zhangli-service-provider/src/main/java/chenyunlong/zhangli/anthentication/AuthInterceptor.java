package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthInterceptor extends AbstractAuthenticationProcessingFilter {
    private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private static final String TOKEN = "Authorization";
    private final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    private final ZhangliProperties zhangliProperties;
    private RedisTemplate redisTemplate;


    public AuthInterceptor(ZhangliProperties zhangliProperties, RedisTemplate redisTemplate) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.zhangliProperties = zhangliProperties;
        this.redisTemplate = redisTemplate;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = getToken(request);
        logger.info(jwtToken, request.getRequestURL());

        if (!StringUtils.isBlank(jwtToken)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Claims claims = Jwts.parser().setSigningKey(zhangliProperties.getSecurity().getSecretKey()).parseClaimsJws(jwtToken.replace("Bearer ", ""))
                        .getBody();
                String key = zhangliProperties.getSecurity().getAuthticationPrefix() + claims.getId();
                String subject = claims.getSubject();

            } catch (JwtException exp) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = new HashMap<>();
                map.put("code", "-1");
                map.put("msg", exp.getMessage());
                map.put("data", null);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(map));
                return;
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
                exp.printStackTrace();
                return;
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        UsernamePasswordAuthenticationToken authRequest;

        return null;
    }
}
