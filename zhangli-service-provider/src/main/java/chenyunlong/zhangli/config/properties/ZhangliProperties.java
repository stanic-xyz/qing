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
    private FileProperties file = new FileProperties();
    private SecurityProperties security = new SecurityProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private String authenticationPrefix;
    private EmailProperties email = new EmailProperties();
    private boolean emailEnabled;
    private String cache;

    public ZhangliProperties() {
    }

    public ZhangliProperties(boolean openAopLog, FileProperties file, SecurityProperties security, SwaggerProperties swagger, String authenticationPrefix) {
        this.openAopLog = openAopLog;
        this.file = file;
        this.security = security;
        this.swagger = swagger;
        this.authenticationPrefix = authenticationPrefix;
    }
}

