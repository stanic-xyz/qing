package chenyunlong.zhangli.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        //这里进行一些配置
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    @Bean
    public AuthGithubRequest getAuthRequest() {

        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("c9391500bdf102edd70c")
                .clientSecret("c2a9c47006fbc8d16b7e8186b10c89c6cc02ab7f")
                .redirectUri("http://localhost:8080/authrize/access_token")
                .build());
    }
}
