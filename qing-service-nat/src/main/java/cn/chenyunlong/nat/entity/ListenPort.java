/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.nat.entity;

import cn.chenyunlong.nat.enumeration.ListenStatusEnum;
import cn.chenyunlong.nat.enumeration.PortTypeEnum;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Data
@Slf4j
public class ListenPort implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * @author Pluto
     * @since 2020-01-10 11:44:41
     */
    public PortTypeEnum getPortTypeEnum() {
        return PortTypeEnum.getEnumByCode(this.portType);
    }

    /**
     * 证书配置状态
     *
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
                        recvSocket != null && recvSocket.isBound() && recvSocket.isConnected() && !recvSocket.isClosed()
                                && !recvSocket.isInputShutdown() && !recvSocket.isOutputShutdown());

                Socket sendSocket = value.getSendSocket();
                model.put("sendSocket", sendSocket == null ? "null"
                        : sendSocket.getLocalPort() + " -> " + sendSocket.getRemoteSocketAddress());
                model.put("sendSocketValid",
                        recvSocket != null && sendSocket.isBound() && sendSocket.isConnected() && !sendSocket.isClosed()
                                && !sendSocket.isInputShutdown() && !sendSocket.isOutputShutdown());

                socketPartJson.put(entry.getKey(), model);
            } catch (Exception e) {
                log.error("格式化异常", e);
            }

        }

        json.put("socketPartMap", socketPartJson);
        return json;
    }


}
