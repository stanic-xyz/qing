package cn.chenyunlong.qing.auth.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@ComponentScan(basePackages = {
    "cn.chenyunlong.qing.infrastructure"
})
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
