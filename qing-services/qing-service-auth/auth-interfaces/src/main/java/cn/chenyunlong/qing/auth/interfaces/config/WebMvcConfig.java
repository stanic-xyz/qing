package cn.chenyunlong.qing.auth.interfaces.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC配置：注册限流拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    //    private final RateLimitInterceptor rateLimitInterceptor;
    //
    //    @Override
    //    public void addInterceptors(InterceptorRegistry registry) {
    //        registry.addInterceptor(rateLimitInterceptor)
    //            .addPathPatterns("/**");
    //    }
}
