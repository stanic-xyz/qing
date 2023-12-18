package cn.chenyunlong.qing.config.security.handler;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.security.userdetails.TemporaryUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 定制成功处理器
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        // start: 判断是否为临时用户, 进行相关逻辑的处理
        final Object principal = authentication.getPrincipal();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (principal instanceof TemporaryUser temporaryUser) {
            // 自己的处理逻辑, 如返回 json 数据
            // ...
            response.getWriter().write(mapper.writeValueAsString(JsonResult.success(temporaryUser.toString())));
        } else {
            response.getWriter().write(mapper.writeValueAsString(JsonResult.success(principal)));
        }
    }
}
