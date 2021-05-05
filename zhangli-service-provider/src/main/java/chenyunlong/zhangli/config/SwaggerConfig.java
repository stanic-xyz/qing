package chenyunlong.zhangli.config;

import chenyunlong.zhangli.model.vo.system.UserInfoVO;
import chenyunlong.zhangli.security.support.TokenProvider;
import chenyunlong.zhangli.config.properties.SwaggerProperties;
import chenyunlong.zhangli.config.properties.ZhangliProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Swagger配置信息
 *
 * @author Stan
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final ZhangliProperties zhangliProperties;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public SwaggerConfig(ZhangliProperties zhangliProperties, TokenProvider tokenProvider, ObjectMapper objectMapper) {
        this.zhangliProperties = zhangliProperties;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Bean
    public Docket swaggerApi() throws JsonProcessingException {
        SwaggerProperties swagger = zhangliProperties.getSwagger();

        Collection<SimpleGrantedAuthority> authorities = new LinkedList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserInfoVO userInfoVO = new UserInfoVO();
        //生成一个临时的token
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(objectMapper.writeValueAsString(userInfoVO), "", authorities);
        String token = tokenProvider.createJwtToken(authenticationToken, false);
        //添加一个参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        //根据每个方法名也知道当前方法在设置什么参数
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securitySchemes()))
                .apiInfo(apiInfo(swagger))
                .enable(!zhangliProperties.getSwagger().isDocDisabled());
    }

    private SecurityScheme securitySchemes() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiInfo apiInfo(SwaggerProperties swagger) {
        Contact contact = new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail());
        return new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                contact.toString(),
                swagger.getLicense(),
                swagger.getLicenseUrl());
    }
}
