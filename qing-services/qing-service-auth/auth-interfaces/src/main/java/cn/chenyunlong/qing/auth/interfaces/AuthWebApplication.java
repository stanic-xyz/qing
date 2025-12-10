package cn.chenyunlong.qing.auth.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableCaching
@ComponentScan(basePackages = "cn.chenyunlong.qing.auth")
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
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
