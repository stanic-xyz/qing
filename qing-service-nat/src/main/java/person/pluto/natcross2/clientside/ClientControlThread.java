/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package person.pluto.natcross2.clientside;

import lombok.extern.slf4j.Slf4j;
import person.pluto.natcross2.api.IBelongControl;
import person.pluto.natcross2.api.socketpart.AbsSocketPart;
import person.pluto.natcross2.clientside.adapter.IClientAdapter;
import person.pluto.natcross2.clientside.config.IClientConfig;
import person.pluto.natcross2.clientside.heart.IClientHeartThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 客户端控制服务
 * </p>
 *
 * @author Pluto
 * @since 2019-07-05 10:53:33
 */
@Slf4j
public final class ClientControlThread implements Runnable, IBelongControl {

    private Thread myThread = null;

    private boolean isAlive = false;

    private Map<String, AbsSocketPart> socketPartMap = new HashMap<>();

    private IClientConfig<?, ?> config;

    private IClientHeartThread clientHeartThread;
    private IClientAdapter<?, ?> clientAdapter;

    public ClientControlThread(IClientConfig<?, ?> config) {
        this.config = config;

    }

    /**
     * 触发控制服务
     *
     * @return
     * @throws Exception
     * @author Pluto
     * @since 2019-07-18 19:02:15
     */
    public boolean createControl() throws Exception {
        if (this.clientAdapter == null) {
            this.clientAdapter = this.config.newCreateControlAdapter(this);
        }

        boolean flag = this.clientAdapter.createControl();

        if (!flag) {
            this.stopClient();
            return false;
        }

        this.start();
        return true;
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                // 使用适配器代理执行
                this.clientAdapter.waitMessage();
            } catch (Exception e) {
                log.warn("client control [{}] to server is exception,will stopClient",
                        this.config.getListenServerPort());
                this.stopClient();
            }
        }
    }

    @Override
    public boolean stopSocketPart(String socketPartKey) {
        log.debug("stopSocketPart[{}]", socketPartKey);
        AbsSocketPart socketPart = socketPartMap.get(socketPartKey);
        if (socketPart == null) {
            return false;
        }
        socketPart.cancel();
        socketPartMap.remove(socketPartKey);
        return true;
    }

    /**
     * * 启动
     *
     * @author Pluto
     * @since 2020-01-07 16:13:26
     */
    public void start() {
        this.isAlive = true;
        if (myThread == null || !myThread.isAlive()) {

            if (this.clientHeartThread == null || !this.clientHeartThread.isAlive()) {
                this.clientHeartThread = this.config.newClientHeartThread(this);
                if (this.clientHeartThread != null) {
                    this.clientHeartThread.start();
                }
            }

            myThread = new Thread(this);
            myThread.setName("client-" + this.formatInfo());
            myThread.start();
        }
    }

    /**
     * * 停止客户端监听
     *
     * @author Pluto
     * @since 2019-07-19 09:24:41
     */
    public void stopClient() {
        isAlive = false;

        if (myThread != null) {
            myThread.interrupt();
            myThread = null;
        }
    }

    /**
     * * 全部退出
     *
     * @author Pluto
     * @since 2019-07-19 09:19:43
     */
    public void cancell() {

        stopClient();

        if (this.clientHeartThread != null) {
            try {
                this.clientHeartThread.cancel();
            } catch (Exception e) {
                // do no thing
            }
            this.clientHeartThread = null;
        }

        if (clientAdapter != null) {
            try {
                clientAdapter.close();
            } catch (Exception e) {
                // do no thing
            }
            this.clientAdapter = null;
        }

        Set<String> keySet = socketPartMap.keySet();
        String[] array = keySet.toArray(new String[0]);

        for (String key : array) {
            stopSocketPart(key);
        }

    }

    /**
     * * 服务端监听的端口
     *
     * @return
     * @author Pluto
     * @since 2020-01-07 16:13:47
     */
    public Integer getListenServerPort() {
        return config.getListenServerPort();
    }

    /**
     * * 重设目标端口
     *
     * @param destIp
     * @param destPort
     * @author Pluto
     * @since 2020-01-07 16:14:06
     */
    public void setDestIpPort(String destIp, Integer destPort) {
        this.config.setDestIpPort(destIp, destPort);
    }

    /**
     * * 检测是否还活着
     *
     * @return
     * @author Pluto
     * @since 2020-01-07 16:14:21
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * 发送心跳测试
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-07 15:54:47
     */
    public void sendUrgentData() throws Exception {
        // 无需判空，空指针异常也是异常
        this.clientAdapter.sendUrgentData();
    }

    /**
     * 设置隧道伙伴
     *
     * @param socketPartKey
     * @param socketPart
     * @author Pluto
     * @since 2020-01-08 16:35:06
     */
    public void putSocketPart(String socketPartKey, AbsSocketPart socketPart) {
        this.socketPartMap.put(socketPartKey, socketPart);
    }

    /**
     * 格式化信息
     *
     * @return
     * @author Pluto
     * @since 2020-04-15 14:14:49
     */
    public String formatInfo() {
        return String.valueOf(this.getListenServerPort());
    }

}
