package chenyunlong.zhangli.config.properties;

public class SecurityProperties {

    private String anonUrl;

    //token默认有效时间 1天
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

    public String getAnonUrl() {
        return this.anonUrl;
    }

    public Long getJwtTimeOut() {
        return this.jwtTimeOut;
    }

    public String getAuthticationPrefix() {
        return this.authticationPrefix;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public long getTokenValidityInMilliseconds() {
        return this.tokenValidityInMilliseconds;
    }

    public long getTokenValidityInMillisecondsForRememberMe() {
        return this.tokenValidityInMillisecondsForRememberMe;
    }

    public void setAnonUrl(String anonUrl) {
        this.anonUrl = anonUrl;
    }

    public void setJwtTimeOut(Long jwtTimeOut) {
        this.jwtTimeOut = jwtTimeOut;
    }

    public void setAuthticationPrefix(String authticationPrefix) {
        this.authticationPrefix = authticationPrefix;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setTokenValidityInMilliseconds(long tokenValidityInMilliseconds) {
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    public void setTokenValidityInMillisecondsForRememberMe(long tokenValidityInMillisecondsForRememberMe) {
        this.tokenValidityInMillisecondsForRememberMe = tokenValidityInMillisecondsForRememberMe;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SecurityProperties)) return false;
        final SecurityProperties other = (SecurityProperties) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$anonUrl = this.getAnonUrl();
        final Object other$anonUrl = other.getAnonUrl();
        if (this$anonUrl == null ? other$anonUrl != null : !this$anonUrl.equals(other$anonUrl)) return false;
        final Object this$jwtTimeOut = this.getJwtTimeOut();
        final Object other$jwtTimeOut = other.getJwtTimeOut();
        if (this$jwtTimeOut == null ? other$jwtTimeOut != null : !this$jwtTimeOut.equals(other$jwtTimeOut))
            return false;
        final Object this$authticationPrefix = this.getAuthticationPrefix();
        final Object other$authticationPrefix = other.getAuthticationPrefix();
        if (this$authticationPrefix == null ? other$authticationPrefix != null : !this$authticationPrefix.equals(other$authticationPrefix))
            return false;
        final Object this$secretKey = this.getSecretKey();
        final Object other$secretKey = other.getSecretKey();
        if (this$secretKey == null ? other$secretKey != null : !this$secretKey.equals(other$secretKey)) return false;
        if (this.getTokenValidityInMilliseconds() != other.getTokenValidityInMilliseconds()) return false;
        if (this.getTokenValidityInMillisecondsForRememberMe() != other.getTokenValidityInMillisecondsForRememberMe())
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $anonUrl = this.getAnonUrl();
        result = result * PRIME + ($anonUrl == null ? 43 : $anonUrl.hashCode());
        final Object $jwtTimeOut = this.getJwtTimeOut();
        result = result * PRIME + ($jwtTimeOut == null ? 43 : $jwtTimeOut.hashCode());
        final Object $authticationPrefix = this.getAuthticationPrefix();
        result = result * PRIME + ($authticationPrefix == null ? 43 : $authticationPrefix.hashCode());
        final Object $secretKey = this.getSecretKey();
        result = result * PRIME + ($secretKey == null ? 43 : $secretKey.hashCode());
        final long $tokenValidityInMilliseconds = this.getTokenValidityInMilliseconds();
        result = result * PRIME + (int) ($tokenValidityInMilliseconds >>> 32 ^ $tokenValidityInMilliseconds);
        final long $tokenValidityInMillisecondsForRememberMe = this.getTokenValidityInMillisecondsForRememberMe();
        result = result * PRIME + (int) ($tokenValidityInMillisecondsForRememberMe >>> 32 ^ $tokenValidityInMillisecondsForRememberMe);
        return result;
    }

    public String toString() {
        return "SecurityProperties(anonUrl=" + this.getAnonUrl() + ", jwtTimeOut=" + this.getJwtTimeOut() + ", authticationPrefix=" + this.getAuthticationPrefix() + ", secretKey=" + this.getSecretKey() + ", tokenValidityInMilliseconds=" + this.getTokenValidityInMilliseconds() + ", tokenValidityInMillisecondsForRememberMe=" + this.getTokenValidityInMillisecondsForRememberMe() + ")";
    }
}
