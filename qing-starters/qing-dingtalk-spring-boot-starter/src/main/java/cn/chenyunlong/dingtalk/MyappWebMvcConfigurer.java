package cn.chenyunlong.dingtalk;

import cn.chenyunlong.dingtalk.controller.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册授权拦截器。
 *
 * @author Stan
 * @since 2022/10/2022/10/20
 */
@Component
public class MyappWebMvcConfigurer implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Autowired
    public MyappWebMvcConfigurer(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
