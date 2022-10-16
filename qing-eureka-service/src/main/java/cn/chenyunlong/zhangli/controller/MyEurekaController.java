package cn.chenyunlong.zhangli.controller;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContext;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("myEureka")
public class MyEurekaController {

    @Resource
    private PeerAwareInstanceRegistry instanceRegistry;
    @Resource
    private EurekaServerContext eurekaServerContext;
    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("listApplications")
    public Applications getAllServerInfo() {
        return instanceRegistry.getApplications();
    }

    @GetMapping("listServices")
    public List<String> getAllServiceIds() {
        return discoveryClient.getServices();
    }

    @GetMapping("getServiceById")
    public List<ServiceInstance> getServiceById(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
