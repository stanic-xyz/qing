package cn.chenyunlong.qing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.domain")//扫描 @Repository 注解
@EntityScan(basePackages = "cn.chenyunlong.qing.domain")//扫描 @Entity 注解
public class QingBootstrapServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingBootstrapServiceApplication.class, args);
    }

}
