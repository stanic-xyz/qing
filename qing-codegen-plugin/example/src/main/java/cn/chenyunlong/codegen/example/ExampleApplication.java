package cn.chenyunlong.codegen.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 代码生成器示例应用启动类
 * 
 * @author chenyunlong
 * @since 1.0.0
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}