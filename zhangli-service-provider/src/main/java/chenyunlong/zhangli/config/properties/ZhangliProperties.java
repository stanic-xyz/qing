package chenyunlong.zhangli.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author stan
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "zhangli")
public class ZhangliProperties {
    private boolean openAopLog = true;
    private FileProperties file;
    private SecurityProperties security;
    private SwaggerProperties swagger;
    private String authticationPrefix;

    public ZhangliProperties() {
    }

    public ZhangliProperties(boolean openAopLog, FileProperties file, SecurityProperties security, SwaggerProperties swagger, String authticationPrefix) {
        this.openAopLog = openAopLog;
        this.file = file;
        this.security = security;
        this.swagger = swagger;
        this.authticationPrefix = authticationPrefix;
    }

}

