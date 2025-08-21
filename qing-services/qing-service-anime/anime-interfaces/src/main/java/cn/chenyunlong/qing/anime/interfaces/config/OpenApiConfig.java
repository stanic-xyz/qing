package cn.chenyunlong.qing.anime.interfaces.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建OpenApi配置
 */
@Configuration
public class OpenApiConfig {

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
            .title("青动漫服务API")
            .version("1.0.0")
            .description("青动漫服务API文档，提供动漫信息管理、分类管理、剧集管理等功能")
            .termsOfService("https://github.com/chenyunlong/qing")
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
