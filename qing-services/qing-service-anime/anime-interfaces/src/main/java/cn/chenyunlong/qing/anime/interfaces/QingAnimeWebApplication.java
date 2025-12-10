package cn.chenyunlong.qing.anime.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.chenyunlong.qing.anime", "cn.chenyunlong.qing.infrastructure"})
public class QingAnimeWebApplication {

    /**
     * 应用启动类。
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(QingAnimeWebApplication.class, args);
    }

}
