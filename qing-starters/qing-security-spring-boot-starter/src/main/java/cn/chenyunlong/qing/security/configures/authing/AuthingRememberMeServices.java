package cn.chenyunlong.qing.security.configures.authing;

import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;

public class AuthingRememberMeServices implements RememberMeServices {

    public AuthingRememberMeServices(AuthingProperties authingProperty) {
    }

    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {

    }
}
