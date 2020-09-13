package chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Stan
 */
@EnableEurekaClient
@SpringBootApplication
public class ZhangliServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhangliServiceProviderApplication.class, args);
    }
}
