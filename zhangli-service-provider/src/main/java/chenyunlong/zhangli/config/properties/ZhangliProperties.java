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
@NoArgsConstructor
@AllArgsConstructor
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
    /**
     * 首页展示的个数
     */
    private int indexSize = 26;
    /**
     * 展示的搜索年份数量
     */
    private int yearCount = 10;
}

