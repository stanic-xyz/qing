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

package person.pluto.natcross2.serverside.listen.serversocket;

import java.net.ServerSocket;

/**
 * <p>
 * 创建服务端口接口
 * </p>
 *
 * @author Pluto
 * @since 2020-01-09 13:28:12
 */
public interface ICreateServerSocket {

    /**
     * 创建监听服务
     *
     * @param listenPort 监听端口
     * @return
     * @author Pluto
     * @since 2020-01-09 13:34:16
     */
    ServerSocket createServerSocket(int listenPort) throws Exception;

}
