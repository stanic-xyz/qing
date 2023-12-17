package cn.chenyunlong.security.configures.authing.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qing.security.authing")
public class AuthingProperties {

    /**
     * 第三方登录回调处理 url 前缀 ，也就是 RedirectUrl 的前缀, 不包含 ServletContextPath，默认为 /auth2/login.<br><br>
     */
    private String redirectUrlPrefix = "api/auth2/login";

    /**
     * 第三方登录授权登录 url 前缀, 不包含 ServletContextPath，默认为 /auth2/authorization.<br><br>
     */
    private String authLoginUrlPrefix = "api/auth2/authorization";

    /**
     * 第三方授权登录成功后的默认权限, 多个权限用逗号分开, 默认为: "ROLE_USER"
     */
    private String defaultAuthorities = "ROLE_USER";

    /**
     * 第三方授权登录后如未注册用户是否支持自动注册功能, 默认: true<br>
     */
    private Boolean autoSignUp = true;

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
