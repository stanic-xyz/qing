package cn.chenyunlong.zhangli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Stan
 */
@SpringBootApplication
@EnableEurekaServer
public class QingEurekaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingEurekaServiceApplication.class, args);
    }
}
