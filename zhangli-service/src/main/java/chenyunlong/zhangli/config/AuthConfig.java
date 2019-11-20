package chenyunlong.zhangli.config;

import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    AuthRequest getAuthRequest() {
        AuthRequest authRequest = new AuthGithubRequest(me.zhyd.oauth.config.AuthConfig.builder()
                .clientId("c9391500bdf102edd70c")
                .clientSecret("c2a9c47006fbc8d16b7e8186b10c89c6cc02ab7f")
                .redirectUri("http://localhost:8080/authrize/access_token")
                .build());

        return authRequest;
    }
}
