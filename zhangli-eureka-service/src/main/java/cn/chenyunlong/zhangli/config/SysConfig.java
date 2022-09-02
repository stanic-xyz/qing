package cn.chenyunlong.zhangli.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SysConfig {

    private final ApplicationInfoManager applicationInfoManager;
    private final PeerAwareInstanceRegistry instanceRegistry;
    private final EurekaServerContext eurekaServerContext;

    public SysConfig(ApplicationInfoManager applicationInfoManager, PeerAwareInstanceRegistry instanceRegistry, EurekaServerContext eurekaServerContext) {
        this.applicationInfoManager = applicationInfoManager;
        this.instanceRegistry = instanceRegistry;
        this.eurekaServerContext = eurekaServerContext;
    }

    @Bean
    String getTest() {
        InstanceInfo managerInfo = applicationInfoManager.getInfo();
        System.out.println(managerInfo);
        EurekaServerConfig serverConfig = eurekaServerContext.getServerConfig();
        System.out.println(serverConfig);
        InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
                .setInstanceId("account-001")
                .setHostName("localhost")
                .setIPAddr("127.0.0.1")
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setAppName("account") // 大小写无所谓
                .build();

        InstanceInfo instanceInfo2 = InstanceInfo.Builder.newBuilder()
                .setInstanceId("account-002")
                .setHostName("localhost")
                .setIPAddr("127.0.0.2")
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setAppName("account2") // 大小写无所谓
                .setHomePageUrl("http", "123123")
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .setStatusPageUrl("", "")
                .build();

        instanceRegistry.register(instanceInfo2, false);
        instanceRegistry.register(instanceInfo, false);

        return "test";
    }

}
