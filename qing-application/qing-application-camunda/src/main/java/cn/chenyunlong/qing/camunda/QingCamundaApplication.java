package cn.chenyunlong.qing.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这里不是一个工具类.
 */
@SpringBootApplication
public class QingCamundaApplication {

    protected QingCamundaApplication() {
    }

    /**
     * 启动方法.
     *
     * @param args 启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(QingCamundaApplication.class, args);
    }

}
