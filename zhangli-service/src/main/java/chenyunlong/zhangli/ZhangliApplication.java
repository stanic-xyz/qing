package chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ZhangliApplication {

    public static void main(String[] args) {

        SpringApplication.run(ZhangliApplication.class, args);
    }
}
