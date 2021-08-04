package chenyunlong.zhangli.security;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.core.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author Stan
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final ZhangliProperties zhangliProperties;

    public MyAuthenticationSuccessHandler(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String username = (String) authentication.getPrincipal();
        //构建一个Token
        JwtBuilder builder = Jwts.builder();
        //设置主体信息
        String token = builder.setSubject(username)
                //设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + zhangliProperties.getSecurity().getJwtTimeOut()))
                .setId(authentication.getPrincipal().toString())
                .signWith(SignatureAlgorithm.HS512, zhangliProperties.getSecurity().getSecretKey())
                .compact();
        ApiResult<String> success = ApiResult.success(token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(success));
    }
}
