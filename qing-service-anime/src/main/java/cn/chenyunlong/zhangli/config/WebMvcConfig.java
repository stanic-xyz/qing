package cn.chenyunlong.zhangli.config;

import cn.chenyunlong.zhangli.cache.AbstractStringCacheStore;
import cn.chenyunlong.zhangli.cache.InMemoryCacheStore;
import cn.chenyunlong.zhangli.config.properties.ZhangliProperties;
import cn.chenyunlong.zhangli.intercepter.HostInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC通用配置
 *
 * @author stan
 */
@Slf4j
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
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HostInterceptor(zhangliProperties)).addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractStringCacheStore stringCacheStore() {
        AbstractStringCacheStore stringCacheStore = new InMemoryCacheStore();
        log.info("cache store load impl : [{}]", stringCacheStore.getClass());
        return stringCacheStore;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
          SpringBoot自动配置本身并不会把/swagger-ui.html
          这个路径映射到对应的目录META-INF/resources/下面
          采用WebMvcConfigurerAdapter将swagger的静态文件进行发布;
         */
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
