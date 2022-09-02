package cn.chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Stan
 */
@SpringBootApplication
@EnableEurekaServer
public class ZhangliEurekaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangliEurekaServiceApplication.class, args);
    }
}