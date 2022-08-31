package chenyunlong.zhangli.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author stan
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "zhangli")
@EnableConfigurationProperties(FileProperties.class)
public class ZhangliProperties {

    /**
     * 是否开启aop日志
     */
    private boolean openAopLog = true;
    private FileProperties file;
    private SecurityProperties security = new SecurityProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private String authenticationPrefix;
    private boolean emailEnabled;
    private String cache;
    /**
     * 首页展示的数量
     */
    private int indexSize = 26;
    /**
     * 展示的搜索年份数量
     */
    private int yearCount = 10;

    private boolean isMailEnabled = false;
    private long logTimeoutMs = 1000;
}

