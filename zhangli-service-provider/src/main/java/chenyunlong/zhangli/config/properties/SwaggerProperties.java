package chenyunlong.zhangli.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author stan
 */
@Data
@ConfigurationProperties(prefix = "zhangli.swagger")
public class SwaggerProperties {
    private String basePackage;
    private String title;
    private String description;
    private String version;
    private String author;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;
    private boolean docDisabled;
    private String contactName;
}
