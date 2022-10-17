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

package person.pluto.natcross2.serverside.client.handler;

import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.common.Optional;
import person.pluto.natcross2.serverside.client.adapter.PassValueNextEnum;

/**
 * <p>
 * 传值方式客户端是配置的处理接口
 * </p>
 *
 * @param <R>
 * @param <W>
 * @author Pluto
 * @since 2020-01-08 16:47:40
 */
public interface IPassValueHandler<R, W> {

    /**
     * 处理方法
     *
     * @param socketChannel 交互通道
     * @param optional      可以重设值
     * @return
     * @author Pluto
     * @since 2020-01-08 16:48:01
     */
    PassValueNextEnum proc(SocketChannel<? extends R, ? super W> socketChannel, Optional<? extends R> optional);

}
