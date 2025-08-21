package cn.chenyunlong.qing.auth.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@ComponentScan(basePackages = {
    "cn.chenyunlong.qing.infrastructure",
    "cn.chenyunlong.qing.auth.infrastructure",
    "cn.chenyunlong.qing.auth.domain",
    "cn.chenyunlong.qing.auth.application",
    "cn.chenyunlong.qing.auth.interfaces"
})
@EntityScan(basePackages = {
    "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity"
})
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository")
@SpringBootApplication
public class AuthWebApplication {

    /**
     * 应用启动类。
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(AuthWebApplication.class, args);
    }

}
