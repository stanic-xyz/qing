package chenyunlong.zhangli.config;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.intercepter.HostInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC通用配置
 *
 * @author stan
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ZhangliProperties zhangliProperties;

    public WebMvcConfig(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HostInterceptor(zhangliProperties)).addPathPatterns("/movie/**");
    }
}
