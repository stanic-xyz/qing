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

package person.pluto.natcross2.serverside.client.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import person.pluto.natcross2.channel.SecretInteractiveChannel;
import person.pluto.natcross2.channel.SocketChannel;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.utils.AESUtil;

import java.net.Socket;
import java.security.Key;

/**
 * <p>
 * 隧道过程加密的配置类
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:44:42
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SecretSimpleClientServiceConfig extends SimpleClientServiceConfig {

    /**
     * 签名混淆key
     */
    private String tokenKey;
    /**
     * 隧道过程加密key AES
     */
    private Key aesKey;

    public SecretSimpleClientServiceConfig(Integer listenPort) {
        super(listenPort);
    }

    @Override
    public SocketChannel<? extends InteractiveModel, ? super InteractiveModel> newSocketChannel(Socket listenSocket)
            throws Exception {
        SecretInteractiveChannel channel = new SecretInteractiveChannel();
        channel.setCharset(this.getCharset());
        channel.setTokenKey(tokenKey);
        channel.setAesKey(aesKey);
        channel.setSocket(listenSocket);
        return channel;
    }

    /**
     * BASE64格式设置隧道加密密钥
     *
     * @param aesKey
     * @author Pluto
     * @since 2020-01-08 16:45:25
     */
    public void setBaseAesKey(String aesKey) {
        this.aesKey = AESUtil.createKeyByBase64(aesKey);
    }

}
