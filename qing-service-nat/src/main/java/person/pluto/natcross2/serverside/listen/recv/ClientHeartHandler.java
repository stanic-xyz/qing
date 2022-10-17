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

package person.pluto.natcross2.serverside.listen.recv;

import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.model.enumeration.InteractiveTypeEnum;
import person.pluto.natcross2.model.enumeration.NatcrossResultEnum;

/**
 * <p>
 * 心跳检测
 * </p>
 *
 * @author Pluto
 * @since 2020-04-15 13:02:09
 */
public class ClientHeartHandler implements IRecvHandler<InteractiveModel, InteractiveModel> {

    public static final ClientHeartHandler INSTANCE = new ClientHeartHandler();

    @Override
    public boolean proc(InteractiveModel model,
                        SocketChannel<? extends InteractiveModel, ? super InteractiveModel> channel) throws Exception {
        InteractiveTypeEnum interactiveTypeEnum = InteractiveTypeEnum.getEnumByName(model.getInteractiveType());
        if (!InteractiveTypeEnum.HEART_TEST.equals(interactiveTypeEnum)) {
            return false;
        }
        InteractiveModel sendModel = InteractiveModel.of(model.getInteractiveSeq(), InteractiveTypeEnum.HEART_REPLY,
                NatcrossResultEnum.SUCCESS.toResultModel());

        channel.writeAndFlush(sendModel);

        return true;
    }

}
