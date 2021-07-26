package chenyunlong.zhangli.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stan.zhangli.core.core.support.ApiResult;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Stan
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        ApiResult<String> result;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            result = ApiResult.fail(exception.getMessage());
        } else if (exception instanceof LockedException) {
            result = ApiResult.fail("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            result = ApiResult.fail("证书过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            result = ApiResult.fail("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            result = ApiResult.fail("账户被禁用，请联系管理员!");
        } else {
            result = ApiResult.fail("登录失败!");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
