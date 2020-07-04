package stan.zhangli.natcross.entity;

import stan.zhangli.natcross.enumeration.ListenStatusEnum;
import stan.zhangli.natcross.enumeration.PortTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.pluto.natcross2.api.socketpart.AbsSocketPart;
import person.pluto.natcross2.serverside.listen.ServerListenThread;

import java.io.Serializable;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * 监听模型
 * </p>
 *
 * @author Pluto
 * @since 2019-07-22 13:55:39
 */
public class ListenPort implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ListenPort.class);

    private Integer listenPort;


    private String portDescribe;


    private String destIp;


    private Integer destPort;


    private Boolean onStart;


    private Integer portType;

    @JsonIgnore

    private String certPath;

    @JsonIgnore

    private String certPassword;


    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModify;

    /**
     * 监听状态
     */
    @JsonIgnore

    private ServerListenThread serverListenThread;

    public ListenPort() {
    }

    /**
     * 获取监听状态
     *
     * @return
     * @author Pluto
     * @since 2019-07-22 14:51:37
     */
    public ListenStatusEnum getListenStatus() {
        if (serverListenThread == null) {
            return ListenStatusEnum.STOP;
        }
        if (!serverListenThread.isAlive()) {
            return ListenStatusEnum.WAIT;
        }
        return ListenStatusEnum.RUNNING;
    }

    /**
     * 端口类型
     *
     * @return
     * @author Pluto
     * @since 2020-01-10 11:44:41
     */
    public PortTypeEnum getPortTypeEnum() {
        return PortTypeEnum.getEnumByCode(this.portType);
    }

    /**
     * 端口类型
     *
     * @return
     * @author Pluto
     * @since 2020-01-10 13:50:41
     */
    public Integer getPortType() {
        return this.getPortTypeEnum().getCode();
    }

    /**
     * 证书配置状态
     *
     * @return
     * @author Pluto
     * @since 2020-01-10 13:50:15
     */
    public String getCertStatus() {
        if (StringUtils.isAnyBlank(this.certPath, this.certPassword)) {
            return "默认配置";
        } else {
            return this.getCertPath();
        }
    }

    /**
     * 获取ServerListenThread可暴露数据
     *
     * @return
     * @author Pluto
     * @since 2020-04-12 13:05:51
     */
    public JSONObject getServerListenInfo() {
        if (serverListenThread == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        json.put("socketPartList", serverListenThread.getSocketPartList());

        JSONObject socketPartJson = new JSONObject();

        Map<String, AbsSocketPart> socketPartMap = serverListenThread.getSocketPartMap();
        for (Entry<String, AbsSocketPart> entry : socketPartMap.entrySet()) {

            try {
                AbsSocketPart value = entry.getValue();

                JSONObject model = new JSONObject();
                model.put("valid", value.isValid());
                model.put("createTime", value.getCreateTime());

                Socket recvSocket = value.getRecvSocket();
                model.put("recvSocket", recvSocket == null ? "null"
                        : recvSocket.getLocalPort() + " <- " + recvSocket.getRemoteSocketAddress());
                model.put("recvSocketValid",
                        recvSocket == null ? false
                                : recvSocket.isBound() && recvSocket.isConnected() && !recvSocket.isClosed()
                                && !recvSocket.isInputShutdown() && !recvSocket.isOutputShutdown());

                Socket sendSocket = value.getSendSocket();
                model.put("sendSocket", sendSocket == null ? "null"
                        : sendSocket.getLocalPort() + " -> " + sendSocket.getRemoteSocketAddress());
                model.put("sendSocketValid",
                        recvSocket == null ? false
                                : sendSocket.isBound() && sendSocket.isConnected() && !sendSocket.isClosed()
                                && !sendSocket.isInputShutdown() && !sendSocket.isOutputShutdown());

                socketPartJson.put(entry.getKey(), model);
            } catch (Exception e) {
                log.error("格式化异常", e);
            }

        }

        json.put("socketPartMap", socketPartJson);
        return json;
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

    public String getCertPath() {
        return this.certPath;
    }

    public String getCertPassword() {
        return this.certPassword;
    }

    public LocalDateTime getGmtCreate() {
        return this.gmtCreate;
    }

    public LocalDateTime getGmtModify() {
        return this.gmtModify;
    }

    public ServerListenThread getServerListenThread() {
        return this.serverListenThread;
    }

    public ListenPort setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
        return this;
    }

    public ListenPort setPortDescribe(String portDescribe) {
        this.portDescribe = portDescribe;
        return this;
    }

    public ListenPort setDestIp(String destIp) {
        this.destIp = destIp;
        return this;
    }

    public ListenPort setDestPort(Integer destPort) {
        this.destPort = destPort;
        return this;
    }

    public ListenPort setOnStart(Boolean onStart) {
        this.onStart = onStart;
        return this;
    }

    public ListenPort setPortType(Integer portType) {
        this.portType = portType;
        return this;
    }

    public ListenPort setCertPath(String certPath) {
        this.certPath = certPath;
        return this;
    }

    public ListenPort setCertPassword(String certPassword) {
        this.certPassword = certPassword;
        return this;
    }

    public ListenPort setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public ListenPort setGmtModify(LocalDateTime gmtModify) {
        this.gmtModify = gmtModify;
        return this;
    }

    public ListenPort setServerListenThread(ServerListenThread serverListenThread) {
        this.serverListenThread = serverListenThread;
        return this;
    }

    public String toString() {
        return "ListenPort(listenPort=" + this.getListenPort() + ", portDescribe=" + this.getPortDescribe() + ", destIp=" + this.getDestIp() + ", destPort=" + this.getDestPort() + ", onStart=" + this.getOnStart() + ", portType=" + this.getPortType() + ", certPath=" + this.getCertPath() + ", certPassword=" + this.getCertPassword() + ", gmtCreate=" + this.getGmtCreate() + ", gmtModify=" + this.getGmtModify() + ", serverListenThread=" + this.getServerListenThread() + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ListenPort)) return false;
        final ListenPort other = (ListenPort) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$listenPort = this.getListenPort();
        final Object other$listenPort = other.getListenPort();
        if (this$listenPort == null ? other$listenPort != null : !this$listenPort.equals(other$listenPort))
            return false;
        final Object this$portDescribe = this.getPortDescribe();
        final Object other$portDescribe = other.getPortDescribe();
        if (this$portDescribe == null ? other$portDescribe != null : !this$portDescribe.equals(other$portDescribe))
            return false;
        final Object this$destIp = this.getDestIp();
        final Object other$destIp = other.getDestIp();
        if (this$destIp == null ? other$destIp != null : !this$destIp.equals(other$destIp)) return false;
        final Object this$destPort = this.getDestPort();
        final Object other$destPort = other.getDestPort();
        if (this$destPort == null ? other$destPort != null : !this$destPort.equals(other$destPort)) return false;
        final Object this$onStart = this.getOnStart();
        final Object other$onStart = other.getOnStart();
        if (this$onStart == null ? other$onStart != null : !this$onStart.equals(other$onStart)) return false;
        final Object this$portType = this.getPortType();
        final Object other$portType = other.getPortType();
        if (this$portType == null ? other$portType != null : !this$portType.equals(other$portType)) return false;
        final Object this$certPath = this.getCertPath();
        final Object other$certPath = other.getCertPath();
        if (this$certPath == null ? other$certPath != null : !this$certPath.equals(other$certPath)) return false;
        final Object this$certPassword = this.getCertPassword();
        final Object other$certPassword = other.getCertPassword();
        if (this$certPassword == null ? other$certPassword != null : !this$certPassword.equals(other$certPassword))
            return false;
        final Object this$gmtCreate = this.getGmtCreate();
        final Object other$gmtCreate = other.getGmtCreate();
        if (this$gmtCreate == null ? other$gmtCreate != null : !this$gmtCreate.equals(other$gmtCreate)) return false;
        final Object this$gmtModify = this.getGmtModify();
        final Object other$gmtModify = other.getGmtModify();
        if (this$gmtModify == null ? other$gmtModify != null : !this$gmtModify.equals(other$gmtModify)) return false;
        final Object this$serverListenThread = this.getServerListenThread();
        final Object other$serverListenThread = other.getServerListenThread();
        if (this$serverListenThread == null ? other$serverListenThread != null : !this$serverListenThread.equals(other$serverListenThread))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ListenPort;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $listenPort = this.getListenPort();
        result = result * PRIME + ($listenPort == null ? 43 : $listenPort.hashCode());
        final Object $portDescribe = this.getPortDescribe();
        result = result * PRIME + ($portDescribe == null ? 43 : $portDescribe.hashCode());
        final Object $destIp = this.getDestIp();
        result = result * PRIME + ($destIp == null ? 43 : $destIp.hashCode());
        final Object $destPort = this.getDestPort();
        result = result * PRIME + ($destPort == null ? 43 : $destPort.hashCode());
        final Object $onStart = this.getOnStart();
        result = result * PRIME + ($onStart == null ? 43 : $onStart.hashCode());
        final Object $portType = this.getPortType();
        result = result * PRIME + ($portType == null ? 43 : $portType.hashCode());
        final Object $certPath = this.getCertPath();
        result = result * PRIME + ($certPath == null ? 43 : $certPath.hashCode());
        final Object $certPassword = this.getCertPassword();
        result = result * PRIME + ($certPassword == null ? 43 : $certPassword.hashCode());
        final Object $gmtCreate = this.getGmtCreate();
        result = result * PRIME + ($gmtCreate == null ? 43 : $gmtCreate.hashCode());
        final Object $gmtModify = this.getGmtModify();
        result = result * PRIME + ($gmtModify == null ? 43 : $gmtModify.hashCode());
        final Object $serverListenThread = this.getServerListenThread();
        result = result * PRIME + ($serverListenThread == null ? 43 : $serverListenThread.hashCode());
        return result;
    }
}
