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

package person.pluto.natcross2;

import org.apache.commons.lang3.StringUtils;
import person.pluto.natcross2.CommonConstants.ListenDest;
import person.pluto.natcross2.serverside.client.ClientServiceThread;
import person.pluto.natcross2.serverside.client.config.SecretSimpleClientServiceConfig;
import person.pluto.natcross2.serverside.client.config.SimpleClientServiceConfig;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.config.AllSecretSimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SecretSimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.serversocket.ICreateServerSocket;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.security.KeyStore;

/**
 * <p>
 * 服务端，放在外网侧
 * </p>
 *
 * @author Pluto
 * @since 2020-01-09 16:27:03
 */
public class ServerApp {

    // 你的p12格式的证书路径
    private static final String sslKeyStorePath = System.getenv("sslKeyStorePath");
    // 你的证书密码
    private static final String sslKeyStorePassword = System.getenv("sslKeyStorePassword");

    public static ICreateServerSocket createServerSocket;

    public static void main(String[] args) throws Exception {

        // 如果需要HTTPS协议的支持，则填写sslKeyStorePath、sslKeyStorePassword或在环境变量中定义
        if (StringUtils.isNoneBlank(sslKeyStorePath, sslKeyStorePassword)) {
            createServerSocket = new ICreateServerSocket() {
                @Override
                public ServerSocket createServerSocket(int listenPort) throws Exception {
                    KeyStore kstore = KeyStore.getInstance("PKCS12");
                    kstore.load(new FileInputStream(sslKeyStorePath), sslKeyStorePassword.toCharArray());
                    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("sunx509");
                    keyFactory.init(kstore, sslKeyStorePassword.toCharArray());

                    SSLContext ctx = SSLContext.getInstance("TLSv1.2");
                    ctx.init(keyFactory.getKeyManagers(), null, null);

                    SSLServerSocketFactory serverSocketFactory = ctx.getServerSocketFactory();

                    return serverSocketFactory.createServerSocket(listenPort);
                }
            };
        }
        createServerSocket = null;

//        simple();
        secret();
//        secretAll();
    }

    /**
     * 交互、隧道都进行加密
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:29:26
     */
    public static void secretAll() throws Exception {
        // 设置并启动客户端服务线程
        SecretSimpleClientServiceConfig config = new SecretSimpleClientServiceConfig(CommonConstants.SERVICE_PORT);
        // 设置交互aes密钥和签名密钥
        config.setBaseAesKey(CommonConstants.AES_KEY);
        config.setTokenKey(CommonConstants.TOKEN_KEY);
        new ClientServiceThread(config).start();

        for (ListenDest model : CommonConstants.listenDestArray) {
            AllSecretSimpleListenServerConfig listengConfig = new AllSecretSimpleListenServerConfig(model.listenPort);
            // 设置交互aes密钥和签名密钥，这里使用和客户端服务相同的密钥，可以根据需要设置不同的
            listengConfig.setBaseAesKey(CommonConstants.AES_KEY);
            listengConfig.setTokenKey(CommonConstants.TOKEN_KEY);
            // 设置隧道密钥
            listengConfig.setBasePasswayKey(CommonConstants.AES_KEY);
            listengConfig.setCreateServerSocket(createServerSocket);
            ListenServerControl.createNewListenServer(listengConfig);
        }
    }

    /**
     * 交互加密，即交互验证
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:28:54
     */
    public static void secret() throws Exception {
        // 设置并启动客户端服务线程
        SecretSimpleClientServiceConfig config = new SecretSimpleClientServiceConfig(CommonConstants.SERVICE_PORT);
        // 设置交互aes密钥和签名密钥
        config.setBaseAesKey(CommonConstants.AES_KEY);
        config.setTokenKey(CommonConstants.TOKEN_KEY);
        new ClientServiceThread(config).start();

        for (ListenDest model : CommonConstants.listenDestArray) {
            // 设置并启动一个穿透端口
            SecretSimpleListenServerConfig listengConfig = new SecretSimpleListenServerConfig(model.listenPort);
            // 设置交互aes密钥和签名密钥，这里使用和客户端服务相同的密钥，可以根据需要设置不同的
            listengConfig.setBaseAesKey(CommonConstants.AES_KEY);
            listengConfig.setTokenKey(CommonConstants.TOKEN_KEY);
            listengConfig.setCreateServerSocket(createServerSocket);
            ListenServerControl.createNewListenServer(listengConfig);
        }
    }

    /**
     * 无加密、无验证
     *
     * @throws Exception
     * @author Pluto
     * @since 2020-01-08 17:29:02
     */
    public static void simple() throws Exception {
        // 设置并启动客户端服务线程
        SimpleClientServiceConfig config = new SimpleClientServiceConfig(CommonConstants.SERVICE_PORT);
        new ClientServiceThread(config).start();

        for (ListenDest model : CommonConstants.listenDestArray) {
            // 设置并启动一个穿透端口
            SimpleListenServerConfig listengConfig = new SimpleListenServerConfig(model.listenPort);
            listengConfig.setCreateServerSocket(createServerSocket);
            ListenServerControl.createNewListenServer(listengConfig);
        }
    }

}
