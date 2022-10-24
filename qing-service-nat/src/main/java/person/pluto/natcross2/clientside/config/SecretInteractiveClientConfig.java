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

package person.pluto.natcross2.clientside.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import person.pluto.natcross2.channel.SecretInteractiveChannel;
import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.utils.AESUtil;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;

/**
 * <p>
 * 交互加密的配置方案（AES加密）
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:32:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SecretInteractiveClientConfig extends InteractiveClientConfig {

    private String tokenKey;
    private Key aesKey;

    @Override
    public SocketChannel<? extends InteractiveModel, ? super InteractiveModel> newClientChannel() {
        SecretInteractiveChannel channel = new SecretInteractiveChannel();

        channel.setCharset(this.getCharset());
        channel.setTokenKey(tokenKey);
        channel.setAesKey(aesKey);

        try {
            Socket socket = new Socket(this.getClientServiceIp(), this.getClientServicePort());
            channel.setSocket(socket);
        } catch (IOException e) {
            return null;
        }
        return channel;
    }

    /**
     * 设置交互密钥
     *
     * @param aesKey
     * @author Pluto
     * @since 2020-01-08 16:32:37
     */
    public void setBaseAesKey(String aesKey) {
        this.aesKey = AESUtil.createKeyByBase64(aesKey);
    }

}