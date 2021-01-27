package chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Stan
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ZhangliServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhangliServiceProviderApplication.class, args);
    }
}
