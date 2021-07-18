package chenyunlong.zhangli.common.config;

import chenyunlong.zhangli.common.config.properties.SwaggerProperties;
import chenyunlong.zhangli.common.config.properties.ZhangliProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyPair;

/**
 * 全局配置
 *
 * @author stan
 * @date 2020/09/23
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ZhangliProperties.class, SwaggerProperties.class})
public class ZhangliConfiguration {

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
                .redirectUri("http://localhost:8080/authorize/callback")
                .build());
    }

    @Bean
    RestTemplate restTemplate() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build();
        } catch (Exception e) {
            log.error("初始化错误", e);
        }
        assert sslContext != null;
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2"},
                null,
                NoopHostnameVerifier.INSTANCE);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setSSLSocketFactory(csf)
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        return restTemplate;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }
}
