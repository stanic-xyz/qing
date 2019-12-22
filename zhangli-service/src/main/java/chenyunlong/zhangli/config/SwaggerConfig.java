package chenyunlong.zhangli.config;

import chenyunlong.zhangli.properties.SwaggerProperties;
import chenyunlong.zhangli.properties.ZhangliProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private ZhangliProperties zhangliProperties;

    @Bean
    public Docket swaggerApi() {
        SwaggerProperties swagger = zhangliProperties.getSwagger();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swagger));
    }

    private ApiInfo apiInfo(SwaggerProperties swagger) {
        Contact contact = new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail());
        ApiInfo apiInfo = new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                contact,
                swagger.getLicense(),
                swagger.getLicenseUrl());
        return apiInfo;
    }
}
