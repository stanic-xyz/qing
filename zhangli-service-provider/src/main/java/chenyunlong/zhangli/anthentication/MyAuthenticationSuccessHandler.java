package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.properties.ZhangliProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);
    public static final String RETURN_TYPE = "html"; // 登录成功时，用来判断是返回json数据还是跳转html页面


    private final RedisTemplate redisTemplate;
    private final ZhangliProperties zhangliProperties;

    public MyAuthenticationSuccessHandler(RedisTemplate redisTemplate, SecurityProperties securityProperties, ZhangliProperties zhangliProperties) {
        this.redisTemplate = redisTemplate;
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //缺少这一句，登录不成功
        SecurityContextHolder.getContext().setAuthentication(authentication);
        redisTemplate.opsForValue().set(zhangliProperties.getSecurity().getAuthticationPrefix() + authentication.getPrincipal().toString(), authentication);
        ObjectMapper objectMapper = new ObjectMapper();

        JwtBuilder builder = Jwts.builder();
        builder.setSubject(objectMapper.writeValueAsString(authentication));
        builder.setExpiration(new Date(System.currentTimeMillis() + zhangliProperties.getSecurity().getJwtTimeOut()));
        builder.setId(authentication.getPrincipal().toString());
        String token = builder.signWith(SignatureAlgorithm.HS512, zhangliProperties.getSecurity().getSecretKey()).compact();
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("msg", "登录成功");
        map.put("data", token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
