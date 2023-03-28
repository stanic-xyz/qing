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

package person.pluto.natcross2.serverside.listen.recv;

import person.pluto.natcross2.channel.SocketChannel;

/**
 * <p>
 * 接收处理器
 * </p>
 *
 * @author Pluto
 * @since 2020-04-15 11:13:20
 */
public interface IRecvHandler<R, W> {

    boolean proc(R model, SocketChannel<? extends R, ? super W> channel) throws Exception;

}
