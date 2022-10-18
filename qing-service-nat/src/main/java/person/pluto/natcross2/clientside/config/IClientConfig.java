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

package person.pluto.natcross2.clientside.config;

import person.pluto.natcross2.api.socketpart.AbsSocketPart;
import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.clientside.ClientControlThread;
import person.pluto.natcross2.clientside.adapter.IClientAdapter;
import person.pluto.natcross2.clientside.heart.IClientHeartThread;

/**
 * <p>
 * 客户端配置接口
 * </p>
 *
 * @param <R> 通道读取的类型
 * @param <W> 通道写入的类型
 * @author Pluto
 * @since 2020-01-08 16:30:04
 */
public interface IClientConfig<R, W> {

    /**
     * 获取服务端IP
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 08:56:10
     */
    String getClientServiceIp();

    /**
     * 获取服务端端口
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 08:56:24
     */
    Integer getClientServicePort();

    /**
     * 对应的监听端口
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 08:56:31
     */
    Integer getListenServerPort();

    /**
     * 目标IP
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 08:56:56
     */
    String getDestIp();

    /**
     * 目标端口
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 08:57:03
     */
    Integer getDestPort();

    /**
     * 设置目标IP
     *
     * @param destIp
     * @author Pluto
     * @since 2020-01-08 08:57:16
     */
    void setDestIpPort(String destIp, Integer destPort);

    /**
     * 新建心跳测试线程
     *
     * @param clientControlThread
     * @return
     * @author Pluto
     * @since 2020-01-08 09:03:09
     */
    IClientHeartThread newClientHeartThread(ClientControlThread clientControlThread);

    /**
     * 新建适配器
     *
     * @param clientControlThread
     * @return
     * @author Pluto
     * @since 2020-01-08 09:03:21
     */
    IClientAdapter<R, W> newCreateControlAdapter(ClientControlThread clientControlThread);

    /**
     * 新建与服务端的交互线程
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 09:03:50
     */
    SocketChannel<? extends R, ? super W> newClientChannel();

    /**
     * 创建新的socketPart
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 13:47:17
     */
    AbsSocketPart newSocketPart(ClientControlThread clientControlThread);

}
