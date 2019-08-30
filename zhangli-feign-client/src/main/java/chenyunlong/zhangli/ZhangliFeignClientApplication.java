package chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ZhangliFeignClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(ZhangliFeignClientApplication.class, args);
    }

}
