package cn.chenyunlong.qing.auth.interfaces.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {

    /**
     * jwt 密钥
     */
    private String secret = "qingSecretKey";

    /**
     * JWT有效期（秒）
     */
    private long expiration = 18000; // 5小时
}
