package chenyunlong.zhangli.properties;

import chenyunlong.zhangli.config.FileConfigurationProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "zhangli")
public class ZhangliProperties {

    private boolean openAopLog = true;
    private FileConfigurationProperties file;
    private ShiroProperties shiro;
    private SwaggerProperties swagger = new SwaggerProperties();
}

