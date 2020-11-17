package stan.zhangli.natcross.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stan
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket swaggerApi() {

        //添加一个参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("Authorization").description("用户token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue("Bearer ")
                .required(false)
                //header中的ticket参数非必填，传空也可以
                .build();
        //根据每个方法名也知道当前方法在设置什么参数
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Stanic", "http://www.chenyunlong.cn", "1576302867@qq.com");
        return new ApiInfo(
                "NatCross Service",
                "我自己搭建的内网穿透服务",
                "V1.0.0",
                "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }
}
