package cn.chenyunlong.natcross.config;

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
import cn.chenyunlong.natcross.model.CertModel;
import cn.chenyunlong.natcross.model.SecretModel;

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
