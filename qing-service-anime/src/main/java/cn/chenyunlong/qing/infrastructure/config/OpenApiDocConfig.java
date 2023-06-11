/*
 * Copyright (c) 2019-2023  YunLong Chen
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

import cn.chenyunlong.qing.infrastructure.config.properties.DocProperties;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class OpenApiDocConfig {

    private final DocProperties docProperties;

    @Bean
    public Info info() {
        return new Info()
                .title(docProperties.getTitle())
                .description(docProperties.getDescription())
                .version(docProperties.getVersion())
                .license(new License()
                        .name(docProperties.getLicense())
                        .url(docProperties.getLicenseUrl()));
    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        ExternalDocumentation documentation = new ExternalDocumentation()
                .description(docProperties.getDescription())
                .url("https://wiki.chenyunlong/docs/qing");
        SecurityRequirement securityItem = new SecurityRequirement();
        final String securitySchemeName = "bearerAuth";
        List<Server> servers = docProperties
                .getInfoList()
                .stream()
                .map(serverInfo -> {
                    Server server = new Server();
                    server.setUrl(serverInfo.getUrl());
                    server.setDescription(serverInfo.getDescription());
                    return server;
                })
                .collect(Collectors.toList());
        return new OpenAPI()
                .info(info())
                .servers(servers)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .externalDocs(documentation)
                .addSecurityItem(securityItem)
                // 添加令牌信息
                .components(new Components().addParameters("token", new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .name("token")
                        .description("令牌")
                        .required(true)
                        .example("123456")
                        .schema(new StringSchema())));
    }

    @Bean
    public GroupedOpenApi totalOpenApi() {
        String[] paths = {"/**"};
        return GroupedOpenApi
                .builder()
                .group("全部")
                .addOpenApiCustomiser(getResponseMessage())
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenApiCustomiser getResponseMessage() {
        return openApi -> openApi
                .getPaths()
                .values()
                .forEach(pathItem -> pathItem
                        .readOperations()
                        .forEach(operation -> {
                            ApiResponses apiResponses = operation.getResponses();
                            apiResponses.addApiResponse(String.valueOf(1), new ApiResponse().description("操作成功"));
                            apiResponses.addApiResponse(String.valueOf(0), new ApiResponse().description("操作失败"));
                        }));
    }
}
