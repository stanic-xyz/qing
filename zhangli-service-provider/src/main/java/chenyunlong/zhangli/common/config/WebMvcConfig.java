package chenyunlong.zhangli.common.config;

import chenyunlong.zhangli.cache.AbstractStringCacheStore;
import chenyunlong.zhangli.cache.InMemoryCacheStore;
import chenyunlong.zhangli.common.config.properties.ZhangliProperties;
import chenyunlong.zhangli.intercepter.HostInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("*");
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
}
