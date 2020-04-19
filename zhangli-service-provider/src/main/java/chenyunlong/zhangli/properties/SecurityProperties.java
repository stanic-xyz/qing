package chenyunlong.zhangli.properties;

public class SecurityProperties {

    private String anonUrl;

    //token默认有效时间 1天
    private Long jwtTimeOut = 86400L;
    private String authticationPrefix = "authentication_";

    public SecurityProperties() {
    }

    public SecurityProperties(String anonUrl, Long jwtTimeOut) {
        this.anonUrl = anonUrl;
        this.jwtTimeOut = jwtTimeOut;
    }


    public String getAnonUrl() {
        return anonUrl;
    }

    public void setAnonUrl(String anonUrl) {
        this.anonUrl = anonUrl;
    }

    public Long getJwtTimeOut() {
        return jwtTimeOut;
    }

    public void setJwtTimeOut(Long jwtTimeOut) {
        this.jwtTimeOut = jwtTimeOut;
    }
    

    public String getAuthticationPrefix() {
        return authticationPrefix;
    }

    public void setAuthticationPrefix(String authticationPrefix) {
        this.authticationPrefix = authticationPrefix;
    }
}
