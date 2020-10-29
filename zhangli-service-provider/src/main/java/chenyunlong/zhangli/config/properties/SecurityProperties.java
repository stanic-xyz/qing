package chenyunlong.zhangli.config.properties;

import lombok.*;

/**
 * @author stan
 */
@Data
public class SecurityProperties {

    private String anonUrl;
    /**
     * token有效时间  默认1天
     */
    private Long jwtTimeOut = 10086400L;

    private String authticationPrefix = "auth";
    private String secretKey = "Stanic";

    private long tokenValidityInMilliseconds = 24 * 60 * 1000;
    private long tokenValidityInMillisecondsForRememberMe = 30 * 24 * 60 * 1000;

    public SecurityProperties() {
    }

    public SecurityProperties(String anonUrl, Long jwtTimeOut) {
        this.anonUrl = anonUrl;
        this.jwtTimeOut = jwtTimeOut;
    }


    protected boolean canEqual(final Object other) {
        return other instanceof SecurityProperties;
    }

}
