package cn.chenyunlong.zhangli.config.properties;

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
@ConfigurationProperties(prefix = "zhangli.file")
public class FileProperties {

    private String imageServerUrl;
    private String baseUploadDir;
}
