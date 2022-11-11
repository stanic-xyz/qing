/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.infrastructure.config;

import cn.chenyunlong.qing.infrastructure.config.properties.SwaggerProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiDocConfig {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI springShopOpenAPI() {
        ExternalDocumentation documentation = new ExternalDocumentation()
                .description(swaggerProperties.getDescription())
                .url("https://wiki.chenyunlong/docs/qing");
        Info info = new Info().title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(new License().name(swaggerProperties.getLicense()).url(swaggerProperties.getLicenseUrl()));
        SecurityRequirement securityItem = new SecurityRequirement();
        return new OpenAPI().info(info).externalDocs(documentation).addSecurityItem(securityItem);
    }

    @Bean
    public GroupedOpenApi petOpenApi() {
        String[] paths = {"/api/**"};
        return GroupedOpenApi.builder().group("api").pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi totalOpenApi() {
        String[] paths = {"/**"};
        return GroupedOpenApi.builder().group("全部").pathsToMatch(paths)
                .build();
    }
}
