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

package person.pluto.natcross2.serverside.client.process;

import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;

/**
 * <p>
 * 处理方法接口
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:49:06
 */
public interface IProcess {

    /**
     * 判断是否是由这个处理
     *
     * @param recvInteractiveModel
     * @return
     * @author Pluto
     * @since 2020-01-06 09:51:16
     */
    boolean wouldProc(InteractiveModel recvInteractiveModel);

    /**
     * 处理方法，需要回复信息的，自己使用socketChannel回复
     *
     * @param socketChannel
     * @param recvInteractiveModel
     * @return 是否保持socket开启状态
     * @author Pluto
     * @since 2020-01-06 09:56:43
     */
    boolean processMothed(SocketChannel<? extends InteractiveModel, ? super InteractiveModel> socketChannel,
                          InteractiveModel recvInteractiveModel) throws Exception;

}
