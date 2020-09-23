package chenyunlong.zhangli.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
