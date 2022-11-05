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
import person.pluto.natcross2.common.CommonFormat;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.model.NatcrossResultModel;
import person.pluto.natcross2.model.enumeration.InteractiveTypeEnum;
import person.pluto.natcross2.model.enumeration.NatcrossResultEnum;
import person.pluto.natcross2.model.interactive.ClientConnectModel;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.ServerListenThread;

/**
 * <p>
 * 请求建立隧道处理器
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:48:25
 */
public class ClientConnectProcess implements IProcess {

    @Override
    public boolean wouldProc(InteractiveModel recvInteractiveModel) {
        InteractiveTypeEnum interactiveTypeEnum = InteractiveTypeEnum
                .getEnumByName(recvInteractiveModel.getInteractiveType());
        return InteractiveTypeEnum.CLIENT_CONNECT.equals(interactiveTypeEnum);
    }

    @Override
    public boolean processMothed(SocketChannel<? extends InteractiveModel, ? super InteractiveModel> socketChannel,
                                 InteractiveModel recvInteractiveModel) throws Exception {
        ClientConnectModel clientConnectModel = recvInteractiveModel.getData().toJavaObject(ClientConnectModel.class);
        Integer listenPort = CommonFormat.getSocketPortByPartKey(clientConnectModel.getSocketPartKey());

        ServerListenThread serverListenThread = ListenServerControl.get(listenPort);

        if (serverListenThread == null) {
            socketChannel.writeAndFlush(InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                    InteractiveTypeEnum.COMMON_REPLY, NatcrossResultEnum.NO_HAS_SERVER_LISTEN.toResultModel()));
            return false;
        }

        // 回复设置成功，如果doSetPartClient没有找到对应的搭档，则直接按关闭处理
        socketChannel.writeAndFlush(InteractiveModel.of(recvInteractiveModel.getInteractiveSeq(),
                InteractiveTypeEnum.COMMON_REPLY, NatcrossResultModel.ofSuccess()));

        boolean doSetPartClient = serverListenThread.doSetPartClient(clientConnectModel.getSocketPartKey(),
                socketChannel.getSocket());
        // 若设置成功，则上层无需关闭
        // 若设置失败，则由上层关闭
        return doSetPartClient;
    }

}
