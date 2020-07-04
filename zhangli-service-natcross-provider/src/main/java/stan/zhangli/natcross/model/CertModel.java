package stan.zhangli.natcross.model;

import java.io.File;

/**
 * <p>
 * 证书配置模型
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 11:45:28
 */
public class CertModel {

    /**
     * 证书存放基础路径
     */
    private String basePath;
    /**
     * 默认证书名（支持相对路径）
     */
    private String defaultCertName;
    /**
     * 默认证书密码（明文）
     */
    private String defaultCertPassword;

    public CertModel() {
    }

    public String formatCertPath(String certName) {
        return this.basePath + File.separator + certName;
    }

    public String formatDefaultCertPath() {
        return this.formatCertPath(this.defaultCertName);
    }

    public String getBasePath() {
        return this.basePath;
    }

    public String getDefaultCertName() {
        return this.defaultCertName;
    }

    public String getDefaultCertPassword() {
        return this.defaultCertPassword;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setDefaultCertName(String defaultCertName) {
        this.defaultCertName = defaultCertName;
    }

    public void setDefaultCertPassword(String defaultCertPassword) {
        this.defaultCertPassword = defaultCertPassword;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CertModel)) return false;
        final CertModel other = (CertModel) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$basePath = this.getBasePath();
        final Object other$basePath = other.getBasePath();
        if (this$basePath == null ? other$basePath != null : !this$basePath.equals(other$basePath)) return false;
        final Object this$defaultCertName = this.getDefaultCertName();
        final Object other$defaultCertName = other.getDefaultCertName();
        if (this$defaultCertName == null ? other$defaultCertName != null : !this$defaultCertName.equals(other$defaultCertName))
            return false;
        final Object this$defaultCertPassword = this.getDefaultCertPassword();
        final Object other$defaultCertPassword = other.getDefaultCertPassword();
        if (this$defaultCertPassword == null ? other$defaultCertPassword != null : !this$defaultCertPassword.equals(other$defaultCertPassword))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CertModel;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $basePath = this.getBasePath();
        result = result * PRIME + ($basePath == null ? 43 : $basePath.hashCode());
        final Object $defaultCertName = this.getDefaultCertName();
        result = result * PRIME + ($defaultCertName == null ? 43 : $defaultCertName.hashCode());
        final Object $defaultCertPassword = this.getDefaultCertPassword();
        result = result * PRIME + ($defaultCertPassword == null ? 43 : $defaultCertPassword.hashCode());
        return result;
    }

    public String toString() {
        return "CertModel(basePath=" + this.getBasePath() + ", defaultCertName=" + this.getDefaultCertName() + ", defaultCertPassword=" + this.getDefaultCertPassword() + ")";
    }
}
