package stan.zhangli.natcross.model;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 加密模型，全局使用统一的
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:53:51
 */
public class SecretModel {

    /**
     * base64格式的AES密钥
     */
    private String aeskey;

    /**
     * 交互签名密钥
     */
    private String tokenKey;

    public SecretModel() {
    }

    /**
     * 判断是否启用加密模式
     * 
     * @author Pluto
     * @since 2020-01-10 09:57:55
     * @return
     */
    public boolean isValid() {
        return StringUtils.isNoneBlank(this.getAeskey(), this.getTokenKey());
    }

    public String getAeskey() {
        return this.aeskey;
    }

    public String getTokenKey() {
        return this.tokenKey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SecretModel)) return false;
        final SecretModel other = (SecretModel) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$aeskey = this.aeskey;
        final Object other$aeskey = other.aeskey;
        if (this$aeskey == null ? other$aeskey != null : !this$aeskey.equals(other$aeskey)) return false;
        final Object this$tokenKey = this.tokenKey;
        final Object other$tokenKey = other.tokenKey;
        if (this$tokenKey == null ? other$tokenKey != null : !this$tokenKey.equals(other$tokenKey)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SecretModel;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $aeskey = this.aeskey;
        result = result * PRIME + ($aeskey == null ? 43 : $aeskey.hashCode());
        final Object $tokenKey = this.tokenKey;
        result = result * PRIME + ($tokenKey == null ? 43 : $tokenKey.hashCode());
        return result;
    }

    public String toString() {
        return "SecretModel(aeskey=" + this.aeskey + ", tokenKey=" + this.tokenKey + ")";
    }
}
