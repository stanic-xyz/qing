package chenyunlong.zhangli.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
public class FileConfigurationProperties {

    private String imageServerUrl;
    private String baseUploadDir;
}
