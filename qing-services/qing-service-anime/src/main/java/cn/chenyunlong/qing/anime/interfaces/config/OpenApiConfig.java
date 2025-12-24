package cn.chenyunlong.qing.anime.interfaces.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * doc 配置
     */
    @Bean
    public OpenAPI openAPI() {
        License license = new License().name("Mulan PSL v2")
                .url("https://license.coscl.org.cn/MulanPSL2");
        Info info = new Info()
                .title("青 动漫服务API")
                .version("1.0.0")
                .description("青动漫服务API文档，提供动漫信息管理、分类管理、剧集管理等功能")
                .termsOfService("https://github.com/stanic-xyz/qing")
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

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
