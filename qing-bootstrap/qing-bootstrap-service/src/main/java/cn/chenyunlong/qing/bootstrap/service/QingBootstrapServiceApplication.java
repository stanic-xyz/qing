package cn.chenyunlong.qing.bootstrap.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class, scanBasePackages = "cn.chenyunlong.qing")
public class QingBootstrapServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingBootstrapServiceApplication.class, args);
    }

}
