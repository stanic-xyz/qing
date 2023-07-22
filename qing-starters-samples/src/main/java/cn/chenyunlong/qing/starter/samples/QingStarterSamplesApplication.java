package cn.chenyunlong.qing.starter.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"cn.chenyunlong.qing"})
public class QingStarterSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingStarterSamplesApplication.class, args);
    }

}
