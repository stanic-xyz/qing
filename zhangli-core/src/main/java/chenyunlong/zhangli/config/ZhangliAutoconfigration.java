package chenyunlong.zhangli.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@EnableConfigurationProperties
public class ZhangliAutoconfigration {

    private Logger logger = LoggerFactory.getLogger(ZhangliAutoconfigration.class);
    private ConfigurableEnvironment env;

    @Bean
    ZhangliConfig getZhangliConfig() {
        logger.info("Configuration.zhangli");
        return new ZhangliConfig();
    }
}
