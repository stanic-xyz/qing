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

package cn.chenyunlong.nat.config;

import cn.chenyunlong.nat.model.CertModel;
import cn.chenyunlong.nat.model.SecretModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import person.pluto.natcross2.model.InteractiveModel;
import person.pluto.natcross2.serverside.client.ClientServiceThread;
import person.pluto.natcross2.serverside.client.config.IClientServiceConfig;
import person.pluto.natcross2.serverside.client.config.SecretSimpleClientServiceConfig;
import person.pluto.natcross2.serverside.client.config.SimpleClientServiceConfig;

/**
 * @author Stan
 */
@Slf4j
@Configuration
public class NatcrossSpring {

    @Bean("secret")
    @Primary
    @ConfigurationProperties(prefix = "natcross")
    public SecretModel getSecret() {
        return new SecretModel();
    }

    @Bean("cert")
    @Primary
    @ConfigurationProperties(prefix = "natcross.cert")
    public CertModel getCert() {
        return new CertModel();
    }

    @Bean("clientServiceConfig")
    @Primary
    @ConfigurationProperties(prefix = "natcross.client")
    public IClientServiceConfig<InteractiveModel, InteractiveModel> getClientServiceConfig(
            @Qualifier("secret") SecretModel secret) {

        if (secret.isValid()) {
            SecretSimpleClientServiceConfig secretSimpleClientServiceConfig = new SecretSimpleClientServiceConfig();
            secretSimpleClientServiceConfig.setBaseAesKey(secret.getAeskey());
            secretSimpleClientServiceConfig.setTokenKey(secret.getTokenKey());
            return secretSimpleClientServiceConfig;
        } else {
            return new SimpleClientServiceConfig();
        }
    }

    @Bean("clientServiceThread")
    @Primary
    public ClientServiceThread getClientServiceThread(
            @Qualifier("clientServiceConfig") IClientServiceConfig<InteractiveModel, InteractiveModel> config) {

        ClientServiceThread serviceThread = new ClientServiceThread(config);
        try {
            //启动服务控制端口件监听
            serviceThread.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("启动服务监听端口失败:", exception);
        }

        return serviceThread;
    }

}
