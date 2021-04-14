package com.chenyunlong.zhangli.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ZhangliBusServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangliBusServerApplication.class, args);
    }

}
