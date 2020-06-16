package chenyunlong.zhangli.config;

import chenyunlong.zhangli.properties.SwaggerProperties;
import chenyunlong.zhangli.properties.ZhangliProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ZhangliProperties zhangliProperties;

    public SwaggerConfig(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Bean
    public Docket swaggerApi() {
        SwaggerProperties swagger = zhangliProperties.getSwagger();

        //添加一个参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("my-auth-token").description("用户token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
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
