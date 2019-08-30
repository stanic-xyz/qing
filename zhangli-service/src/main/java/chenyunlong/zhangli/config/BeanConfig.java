package chenyunlong.zhangli.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    @ConfigurationProperties(prefix = "zhangli")
    public FileConfigurationProperties getFileConfigurationProperties() {
        return new FileConfigurationProperties();
    }

}
