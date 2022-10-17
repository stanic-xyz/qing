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
 */

package person.pluto.natcross2.serverside.listen.clear;

import person.pluto.natcross2.serverside.listen.ServerListenThread;

/**
 * <p>
 * 清理无效端口 线程
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:50:09
 */
public interface IClearInvalidSocketPartThread extends Runnable {

    /**
     * 设置附属的穿透线程
     *
     * @param serverListenThread
     * @author Pluto
     * @since 2020-01-08 16:50:22
     */
    void setServerListenThread(ServerListenThread serverListenThread);

    /**
     * 启动
     *
     * @author Pluto
     * @since 2020-01-08 16:50:46
     */
    void start();

    /**
     * 退出
     *
     * @author Pluto
     * @since 2020-01-08 16:50:51
     */
    void cancel();

}
