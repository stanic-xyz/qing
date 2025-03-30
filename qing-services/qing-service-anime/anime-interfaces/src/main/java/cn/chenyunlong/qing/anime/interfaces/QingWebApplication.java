package cn.chenyunlong.qing.anime.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "cn.chenyunlong.qing.anime.interfaces",
    "cn.chenyunlong.qing.anime.infrastructure",
    "cn.chenyunlong.qing.anime.application"
})
@EntityScan(basePackages = {
    "cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity"
})
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository")
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
