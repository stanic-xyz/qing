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

package person.pluto.natcross2;

import person.pluto.natcross2.CommonConstants.ListenDest;
import person.pluto.natcross2.clientside.ClientControlThread;
import person.pluto.natcross2.clientside.config.AllSecretInteractiveClientConfig;
import person.pluto.natcross2.clientside.config.InteractiveClientConfig;
import person.pluto.natcross2.clientside.config.SecretInteractiveClientConfig;

/**
 * <p>
 * 客户端，放在内网侧
 * </p>
 *
 * @author Pluto
 * @since 2020-01-09 16:26:44
 */
public class ClientApp {

    public static void main(String[] args) throws Exception {
//        simple();
        secret();
//        secretAll();
    }

    /**
     * 交互、隧道都进行加密
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:29:54
     */
    public static void secretAll() throws Exception {
        for (ListenDest model : CommonConstants.listenDestArray) {
            AllSecretInteractiveClientConfig config = new AllSecretInteractiveClientConfig();

            // 设置服务端IP和端口
            config.setClientServiceIp(CommonConstants.SERVICE_IP);
            config.setClientServicePort(CommonConstants.SERVICE_PORT);
            // 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
            config.setListenServerPort(model.listenPort);
            // 设置要暴露的目标IP和端口
            config.setDestIp(model.destIp);
            config.setDestPort(model.destPort);

            // 设置交互密钥和签名key
            config.setBaseAesKey(CommonConstants.AES_KEY);
            config.setTokenKey(CommonConstants.TOKEN_KEY);
            // 设置隧道交互密钥
            config.setBasePasswayKey(CommonConstants.AES_KEY);

            new ClientControlThread(config).createControl();
        }
    }

    /**
     * 交互加密，即交互验证
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:30:13
     */
    public static void secret() throws Exception {
        for (ListenDest model : CommonConstants.listenDestArray) {
            SecretInteractiveClientConfig config = new SecretInteractiveClientConfig();

            // 设置服务端IP和端口
            config.setClientServiceIp(CommonConstants.SERVICE_IP);
            config.setClientServicePort(CommonConstants.SERVICE_PORT);
            // 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
            config.setListenServerPort(model.listenPort);
            // 设置要暴露的目标IP和端口
            config.setDestIp(model.destIp);
            config.setDestPort(model.destPort);

            // 设置交互密钥和签名key
            config.setBaseAesKey(CommonConstants.AES_KEY);
            config.setTokenKey(CommonConstants.TOKEN_KEY);

            new ClientControlThread(config).createControl();
        }
    }

    /**
     * 无加密、无验证
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:30:22
     */
    public static void simple() throws Exception {
        for (ListenDest model : CommonConstants.listenDestArray) {
            InteractiveClientConfig config = new InteractiveClientConfig();

            // 设置服务端IP和端口
            config.setClientServiceIp(CommonConstants.SERVICE_IP);
            config.setClientServicePort(CommonConstants.SERVICE_PORT);
            // 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
            config.setListenServerPort(model.listenPort);
            // 设置要暴露的目标IP和端口
            config.setDestIp(model.destIp);
            config.setDestPort(model.destPort);

            new ClientControlThread(config).createControl();
        }
    }

}
