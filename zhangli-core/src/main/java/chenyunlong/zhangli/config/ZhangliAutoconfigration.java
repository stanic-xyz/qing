package chenyunlong.zhangli.config;

import chenyunlong.zhangli.config.ZhangliConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@Configuration
@EnableConfigurationProperties
//@ConditionalOnClass(ZhangliConfig.class)
public class ZhangliAutoconfigration {

    private ConfigurableEnvironment env;

    @Bean
    ZhangliConfig getZhangliConfig() {
        log.info("Configuration.zhangli");
        return new ZhangliConfig();
    }
}
