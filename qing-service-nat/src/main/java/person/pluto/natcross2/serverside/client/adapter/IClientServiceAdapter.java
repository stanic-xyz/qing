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

package person.pluto.natcross2.serverside.client.adapter;

import java.net.Socket;

/**
 * <p>
 * 客户端服务适配器
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:40:35
 */
public interface IClientServiceAdapter {

    /**
     * 处理方法
     *
     * @param listenSocket
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 16:40:43
     */
    void procMethod(Socket listenSocket) throws Exception;

}
