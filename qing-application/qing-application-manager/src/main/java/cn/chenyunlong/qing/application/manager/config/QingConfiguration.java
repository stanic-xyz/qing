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

package cn.chenyunlong.qing.application.manager.config;

import cn.chenyunlong.qing.infrastructure.config.properties.DocProperties;
import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import cn.chenyunlong.qing.infrastructure.intercepter.LoggingRequestInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.security.KeyPair;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import org.apache.http.ssl.SSLContextBuilder;
import org.babyfish.jimmer.jackson.ImmutableModule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 全局配置
 *
 * @author 陈云龙
 * @since 2020/09/23
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class QingConfiguration implements InitializingBean {

    private final QingProperties qingProperties;

    private final DocProperties docProperties;

    @Override
    public void afterPropertiesSet() {
        log.info("配置信息：{}", docProperties.getDescription());
    }

    @Bean
    public ObjectMapper getObjectMapper(ImmutableModule immutableModule) {
        ObjectMapper objectMapper = new ObjectMapper();
        //这里进行一些配置
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.registerModule(immutableModule);
        return objectMapper;
    }

    @Bean
    public AuthGithubRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig
                                         .builder()
                                         .clientId("c9391500bdf102edd70c")
                                         .clientSecret("c2a9c47006fbc8d16b7e8186b10c89c6cc02ab7f")
                                         .redirectUri("http://localhost:8080/authorize/callback")
                                         .build());
    }

    @Bean
    RestTemplate restTemplate() {
        SSLContext sslContext = null;
        try {
            sslContext =
                new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();
        } catch (Exception exception) {
            log.error("初始RestTemplate上下文错误", exception);
        }
        assert sslContext != null;
        RestTemplate restTemplate = new RestTemplate();
        // 打印记录
        restTemplate.setInterceptors(Collections.singletonList(
            new LoggingRequestInterceptor(qingProperties.getLogTimeoutMs())));
        return restTemplate;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
