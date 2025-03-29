package cn.chenyunlong.qing.application.manager;

import org.babyfish.jimmer.client.EnableImplicitApi;
import org.babyfish.jimmer.sql.EnableDtoGeneration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"cn.chenyunlong.qing.domain", "cn.chenyunlong.qing.application.manager.*", "cn.chenyunlong.qing.infrastructure.**"})
@EntityScan(basePackages = {
    "cn.chenyunlong.qing.domain.*",
    "cn.chenyunlong.qing.infrastructure.repository.jpa.entity"})
@EnableDtoGeneration
@EnableImplicitApi
public class QingWebApplication {

    /**
     * 应用启动类。
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(QingWebApplication.class, args);
    }

}
