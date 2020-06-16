package chenyunlong.zhangli.anthentication;

import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.ApiResult;
import cn.hutool.json.JSONUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityProblemSupport implements AuthenticationEntryPoint, AccessDeniedHandler {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //设置response状态码，返回错误信息等
        ApiResult success = ResultUtil.success("退出成功");

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        httpServletResponse.getWriter().write(JSONUtil.toJsonPrettyStr(success));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

//        返回json形式的错误信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("{\"code\":403,\"message\":\"小弟弟，你没有权限访问呀！需要通过前端进行认证哦！\",\"data\":\"\"}");
        response.getWriter().flush();
    }
}
