package cn.chenyunlong.qing.config.security.handler;

import cn.chenyunlong.common.model.JsonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResult<Object> jsonResult = JsonResult.unAuthorized("认证失败，请登录！");
        jsonResult.setDevMessage(authException.getMessage());
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(jsonResult));
        writer.flush();
    }
}

