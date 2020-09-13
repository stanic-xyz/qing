package stan.zhangli.natcross.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
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
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Authorization").description("用户token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue("Bearer ")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

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
                "V0.0.1",
                null,
                contact,
                "",
                "");
    }
}
