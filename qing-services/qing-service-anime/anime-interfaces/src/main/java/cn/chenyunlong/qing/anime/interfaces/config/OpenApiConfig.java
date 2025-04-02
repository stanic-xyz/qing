package cn.chenyunlong.qing.anime.interfaces.config;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建OpenApi配置
 */
@Configuration
public class OpenApiConfig {

    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
            }
        };
    }

    /**
     * 公共的 api 配置
     *
     * @return api配置
     */
    @Bean
    public OpenAPI openAPI() {
        License license = new License().name("Mulan PSL v2")
            .url("https://license.coscl.org.cn/MulanPSL2");
        Info info = new Info()
            .title("XXX用户系统API")
            .version("0.0.2-SNAPSHOT")
            .description("Knife4j集成springdoc-openapi示例")
            .termsOfService("http://doc.xiaominfo.com")
            .license(license);
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(info);
        SecurityScheme scheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .name("Authorization")
            .scheme("bearer");
        // 设置 spring security jwt accessToken 认证的请求头 Authorization: Bearer xxx.xxx.xxx
        openAPI.components(new Components().addSecuritySchemes("Authorization", scheme));
        return openAPI;
    }
}
