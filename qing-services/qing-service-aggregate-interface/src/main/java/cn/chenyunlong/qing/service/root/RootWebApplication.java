package cn.chenyunlong.qing.service.root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RootWebApplication {

    /**
     * 主启动类，可以聚合多个应用 比如同时启动 anime auth 服务
     * <p>
     * 后续可以结合spi机制，完成不同的项目的组合发布
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(RootWebApplication.class, args);
    }

}
