package chenyunlong.zhangli;

import chenyunlong.zhangli.config.ZhangliConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass(ZhangliConfig.class)
@ConditionalOnProperty("zhangli.enable")
public class ZhangliAutoconfigration {

    @Bean
    ZhangliConfig getZhangliConfig() {
        log.debug("Configuration.zhangli");
        return new ZhangliConfig();
    }
}
