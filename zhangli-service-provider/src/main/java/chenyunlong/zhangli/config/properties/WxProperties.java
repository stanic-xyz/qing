package chenyunlong.zhangli.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mp.wx")
public class WxProperties {

    private String appId;
    private String secret;
}
