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

package person.pluto.natcross2.serverside.listen.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import person.pluto.natcross2.channel.SecretInteractiveChannel;
import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.utils.AESUtil;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;

/**
 * <p>
 * 交互加密配置
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:52:51
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SecretSimpleListenServerConfig extends SimpleListenServerConfig {

    private String tokenKey;
    private Key aesKey;

    public SecretSimpleListenServerConfig(Integer listenPort) {
        super(listenPort);
    }

    @Override
    protected SocketChannel<? extends InteractiveModel, ? super InteractiveModel> newControlSocketChannel(
            Socket socket) {
        SecretInteractiveChannel channel = new SecretInteractiveChannel();
        channel.setCharset(this.getCharset());
        channel.setTokenKey(tokenKey);
        channel.setAesKey(aesKey);
        try {
            channel.setSocket(socket);
        } catch (IOException e) {
            return null;
        }
        return channel;
    }

    /**
     * BASE64格式设置交互加密密钥
     *
     * @param key
     * @author Pluto
     * @since 2020-01-08 16:52:25
     */
    public void setBaseAesKey(String aesKey) {
        this.aesKey = AESUtil.createKeyByBase64(aesKey);
    }

}
