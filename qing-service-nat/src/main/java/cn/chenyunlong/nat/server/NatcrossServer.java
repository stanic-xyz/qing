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

package cn.chenyunlong.nat.server;

import cn.chenyunlong.nat.enumeration.PortTypeEnum;
import cn.chenyunlong.nat.model.CertModel;
import cn.chenyunlong.nat.model.SecretModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.config.IListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SecretSimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.config.SimpleListenServerConfig;
import person.pluto.natcross2.serverside.listen.serversocket.ICreateServerSocket;
import cn.chenyunlong.nat.entity.ListenPort;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * <p>
 * 内网穿透综合服务类
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 09:50:07
 */
@Service
public class NatcrossServer {

    @Autowired
    private SecretModel secret;

    @Autowired
    private CertModel certModel;

    /**
     * 获取https监听端口
     *
     * @param sslKeyStorePath     ssl密钥文件保存路径
     * @param sslKeyStorePassword 密钥文件的密码
     * @return 服务端口接口
     * @author Pluto
     * @since 2020-01-10 11:59:33
     */
    private ICreateServerSocket newHttpsCreateServerSocket(String sslKeyStorePath, String sslKeyStorePassword) {
        return listenPort -> {
            try (FileInputStream sslKeyStoreFile = new FileInputStream(sslKeyStorePath)) {
                KeyStore kstore = KeyStore.getInstance("PKCS12");
                kstore.load(sslKeyStoreFile, sslKeyStorePassword.toCharArray());
                KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("sunx509");
                keyFactory.init(kstore, sslKeyStorePassword.toCharArray());

                SSLContext ctx = SSLContext.getInstance("TLSv1.2");
                ctx.init(keyFactory.getKeyManagers(), null, null);

                SSLServerSocketFactory serverSocketFactory = ctx.getServerSocketFactory();
                return serverSocketFactory.createServerSocket(listenPort);
            }
        };
    }

    /**
     * 创建新的监听
     *
     * @param listenPortModel 监听模型
     * @return 响应信息
     * @author Pluto
     * @since 2020-01-10 10:09:24
     */
    public boolean createNewListen(ListenPort listenPortModel) {
        SimpleListenServerConfig config;

        if (secret.isValid()) {
            SecretSimpleListenServerConfig secretConfig = new SecretSimpleListenServerConfig(
                    listenPortModel.getListenPort());
            secretConfig.setBaseAesKey(secret.getAeskey());
            secretConfig.setTokenKey(secret.getTokenKey());

            config = secretConfig;
        } else {
            config = new SimpleListenServerConfig(listenPortModel.getListenPort());
        }

        if (PortTypeEnum.HTTPS.equals(listenPortModel.getPortTypeEnum())) {
            String sslKeyStoreFileName;
            String sslKeyStorePassword;

            if (StringUtils.isAnyBlank(listenPortModel.getCertPath(), listenPortModel.getCertPassword())) {
                sslKeyStoreFileName = certModel.getDefaultCertName();
                sslKeyStorePassword = certModel.getDefaultCertPassword();
            } else {
                sslKeyStoreFileName = listenPortModel.getCertPath();
                sslKeyStorePassword = listenPortModel.getCertPassword();
            }

            config.setCreateServerSocket(this.newHttpsCreateServerSocket(certModel.formatCertPath(sslKeyStoreFileName),
                    sslKeyStorePassword));
        }

        return ListenServerControl.createNewListenServer(config) != null;
    }

    /**
     * 创建新的监听
     *
     * @param listenPort 监听端口信息
     * @return 是否创建成功
     * @author Pluto
     * @since 2020-01-10 10:09:37
     */
    public boolean createNewListen(int listenPort) {
        IListenServerConfig config;

        if (secret.isValid()) {
            SecretSimpleListenServerConfig secretConfig = new SecretSimpleListenServerConfig(listenPort);
            secretConfig.setBaseAesKey(secret.getAeskey());
            secretConfig.setTokenKey(secret.getTokenKey());

            config = secretConfig;
        } else {
            config = new SimpleListenServerConfig(listenPort);
        }

        return ListenServerControl.createNewListenServer(config) != null;
    }

    /**
     * 移除某个监听
     *
     * @param listenPort 监听端口
     * @return 端口监听信息
     * @author Pluto
     * @since 2020-01-10 10:10:52
     */
    public boolean removeListen(int listenPort) {
        return ListenServerControl.remove(listenPort);
    }

}
