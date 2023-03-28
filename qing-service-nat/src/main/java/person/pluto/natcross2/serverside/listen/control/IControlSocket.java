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

package person.pluto.natcross2.serverside.listen.control;

import person.pluto.natcross2.serverside.listen.ServerListenThread;

/**
 * <p>
 * 控制端口接口
 * </p>
 *
 * @author Pluto
 * @since 2020-01-07 09:52:51
 */
public interface IControlSocket {

    /**
     * 是否有效
     *
     * @return
     * @author Pluto
     * @since 2020-01-08 16:54:13
     */
    boolean isValid();

    /**
     * 发送隧道等待状态
     *
     * @param socketPartKey
     * @return
     * @author Pluto
     * @since 2020-01-08 16:54:18
     */
    boolean sendClientWait(String socketPartKey);

    /**
     * 关闭
     *
     * @author Pluto
     * @since 2020-01-08 16:54:40
     */
    void close();

    /**
     * 开启接收线程
     *
     * @author Pluto
     * @since 2020-04-15 11:36:44
     */
    void startRecv();

    /**
     * 设置控制的监听线程
     *
     * @param serverListenThread
     * @author Pluto
     * @since 2020-04-15 13:10:25
     */
    void setServerListen(ServerListenThread serverListenThread);

}
