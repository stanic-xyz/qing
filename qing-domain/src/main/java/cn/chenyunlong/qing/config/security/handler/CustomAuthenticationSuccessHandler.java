package cn.chenyunlong.qing.config.security.handler;

import cn.chenyunlong.qing.config.security.utils.JwtTokenUtil;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.security.userdetails.TemporaryUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 定制成功处理器
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthingProperties authingProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // start: 判断是否为临时用户, 进行相关逻辑的处理
        Object principal = authentication.getPrincipal();
        String token = "";
        boolean tempUser = false;
        if (principal instanceof TemporaryUser temporaryUser) {
            token = jwtTokenUtil.generateToken(temporaryUser);
            tempUser = true;
        } else if (principal instanceof User user) {
            token = jwtTokenUtil.generateToken(user);
        }
        response.sendRedirect(authingProperties.getLoginPage() + "?accessToken=%s&tempUser=%s".formatted(token, tempUser));
    }
}
