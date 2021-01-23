package chenyunlong.zhangli.common.config.properties;

import lombok.*;

/**
 * @author stan
 */
@Data
public class SecurityProperties {

    /**
     * 匿名访问地址
     */
    private String anonUrl;
    /**
     * token有效时间  默认1天
     */
    private long jwtTimeOut = 10086400L;
    /**
     * 安全认证前缀
     */
    private String authenticationPrefix = "auth";
    /**
     * 安全密钥
     */
    private String secretKey = "Stanic";
    /**
     * 一般密钥过期时间
     */
    private long tokenValidityInMilliseconds = 24 * 60 * 1000;
    /**
     * “记住我”密钥过期时间
     */
    private long tokenValidityInMillisecondsForRememberMe = 30 * 24 * 60 * 1000;
}
