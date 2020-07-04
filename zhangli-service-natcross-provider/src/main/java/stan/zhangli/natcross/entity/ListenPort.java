package stan.zhangli.natcross.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import person.pluto.natcross2.api.socketpart.AbsSocketPart;
import person.pluto.natcross2.serverside.listen.ServerListenThread;
import stan.zhangli.natcross.enumeration.ListenStatusEnum;
import stan.zhangli.natcross.enumeration.PortTypeEnum;

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

    public Integer getPortType() {
        return this.portType;
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

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModify(LocalDateTime gmtModify) {
        this.gmtModify = gmtModify;
    }

    public void setServerListenThread(ServerListenThread serverListenThread) {
        this.serverListenThread = serverListenThread;
    }

    public String toString() {
        return "ListenPort(listenPort=" + this.listenPort + ", portDescribe=" + this.portDescribe + ", destIp=" + this.destIp + ", destPort=" + this.destPort + ", onStart=" + this.onStart + ", portType=" + this.portType + ", certPath=" + this.certPath + ", certPassword=" + this.certPassword + ", gmtCreate=" + this.gmtCreate + ", gmtModify=" + this.gmtModify + ", serverListenThread=" + this.serverListenThread + ")";
    }
}
