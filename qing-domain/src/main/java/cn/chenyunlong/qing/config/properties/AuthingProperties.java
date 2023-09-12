package cn.chenyunlong.qing.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qing.authing")
public class AuthingProperties {

    private String appName = "stanic";

    /**
     * Authing 应用 ID
     */
    private String appId;

    /**
     * Authing 应用密钥
     */
    private String appSecret;

    /**
     * Authing 应用域名，如 <a href="https://example.authing.cn">...</a>。
     * 注意：Host 地址为示例样式，不同版本用户池的应用 Host 地址形式有所差异，实际地址以 自建应用->应用配置->认证配置 下 `认证地址 `字段为准。
     */
    private String appHost;

    /**
     * Authing 应用配置的登录回调地址
     */
    private String redirectUrl;
}
