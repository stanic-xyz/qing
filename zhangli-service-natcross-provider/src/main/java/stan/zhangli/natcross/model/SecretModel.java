package stan.zhangli.natcross.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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
     * @return 返回是否启用加密模式
     * @author Pluto
     * @since 2020-01-10 09:57:55
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

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SecretModel)) {
            return false;
        }
        final SecretModel other = (SecretModel) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object thisAeskey = this.aeskey;
        final Object otherAeskey = other.aeskey;
        if (!Objects.equals(thisAeskey, otherAeskey)) {
            return false;
        }
        final Object thisTokenKey = this.tokenKey;
        final Object otherTokenKey = other.tokenKey;
        return Objects.equals(thisTokenKey, otherTokenKey);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SecretModel;
    }

    @Override
    public int hashCode() {
        final int prime = 59;
        int result = 1;
        final Object aeskey = this.aeskey;
        result = result * prime + (aeskey == null ? 43 : aeskey.hashCode());
        final Object tokenKey = this.tokenKey;
        result = result * prime + (tokenKey == null ? 43 : tokenKey.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SecretModel(aeskey=" + this.aeskey + ", tokenKey=" + this.tokenKey + ")";
    }
}
