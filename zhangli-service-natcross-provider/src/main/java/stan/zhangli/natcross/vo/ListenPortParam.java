package stan.zhangli.natcross.vo;

import javax.validation.constraints.NotEmpty;

public class ListenPortParam {
    @NotEmpty
    private Integer listenPort;
    private String portDescribe;
    private String destIp;
    private Integer destPort;
    private Boolean onStart;
    private Integer portType;
    private String certPath;
    private String certPassword;

    public ListenPortParam() {
    }

    public Integer getListenPort() {
        return this.listenPort;
    }

    public String getPortDescribe() {
        return this.portDescribe;
    }

    public String getDestIp() {
        return this.destIp;
    }

    public Integer getDestPort() {
        return this.destPort;
    }

    public Boolean getOnStart() {
        return this.onStart;
    }

    public Integer getPortType() {
        return this.portType;
    }

    public String getCertPath() {
        return this.certPath;
    }

    public String getCertPassword() {
        return this.certPassword;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public void setPortDescribe(String portDescribe) {
        this.portDescribe = portDescribe;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    public void setOnStart(Boolean onStart) {
        this.onStart = onStart;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ListenPortParam)) return false;
        final ListenPortParam other = (ListenPortParam) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$listenPort = this.listenPort;
        final Object other$listenPort = other.listenPort;
        if (this$listenPort == null ? other$listenPort != null : !this$listenPort.equals(other$listenPort))
            return false;
        final Object this$portDescribe = this.portDescribe;
        final Object other$portDescribe = other.portDescribe;
        if (this$portDescribe == null ? other$portDescribe != null : !this$portDescribe.equals(other$portDescribe))
            return false;
        final Object this$destIp = this.destIp;
        final Object other$destIp = other.destIp;
        if (this$destIp == null ? other$destIp != null : !this$destIp.equals(other$destIp)) return false;
        final Object this$destPort = this.destPort;
        final Object other$destPort = other.destPort;
        if (this$destPort == null ? other$destPort != null : !this$destPort.equals(other$destPort)) return false;
        final Object this$onStart = this.onStart;
        final Object other$onStart = other.onStart;
        if (this$onStart == null ? other$onStart != null : !this$onStart.equals(other$onStart)) return false;
        final Object this$portType = this.portType;
        final Object other$portType = other.portType;
        if (this$portType == null ? other$portType != null : !this$portType.equals(other$portType)) return false;
        final Object this$certPath = this.certPath;
        final Object other$certPath = other.certPath;
        if (this$certPath == null ? other$certPath != null : !this$certPath.equals(other$certPath)) return false;
        final Object this$certPassword = this.certPassword;
        final Object other$certPassword = other.certPassword;
        if (this$certPassword == null ? other$certPassword != null : !this$certPassword.equals(other$certPassword))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ListenPortParam;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $listenPort = this.listenPort;
        result = result * PRIME + ($listenPort == null ? 43 : $listenPort.hashCode());
        final Object $portDescribe = this.portDescribe;
        result = result * PRIME + ($portDescribe == null ? 43 : $portDescribe.hashCode());
        final Object $destIp = this.destIp;
        result = result * PRIME + ($destIp == null ? 43 : $destIp.hashCode());
        final Object $destPort = this.destPort;
        result = result * PRIME + ($destPort == null ? 43 : $destPort.hashCode());
        final Object $onStart = this.onStart;
        result = result * PRIME + ($onStart == null ? 43 : $onStart.hashCode());
        final Object $portType = this.portType;
        result = result * PRIME + ($portType == null ? 43 : $portType.hashCode());
        final Object $certPath = this.certPath;
        result = result * PRIME + ($certPath == null ? 43 : $certPath.hashCode());
        final Object $certPassword = this.certPassword;
        result = result * PRIME + ($certPassword == null ? 43 : $certPassword.hashCode());
        return result;
    }

    public String toString() {
        return "ListenPort(listenPort=" + this.listenPort + ", portDescribe=" + this.portDescribe + ", destIp=" + this.destIp + ", destPort=" + this.destPort + ", onStart=" + this.onStart + ", portType=" + this.portType + ", certPath=" + this.certPath + ", certPassword=" + this.certPassword + ")";
    }
}
