package cn.chenyunlong.qing.workflow;

import cn.chenyunlong.qing.workflow.listender.EnvPreparedListener;
import cn.chenyunlong.qing.workflow.listender.StartingEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class WorkflowApplication {

    public static void main(String[] args) {
        log.info("=== SpringBoot正在启动，绝对的第一行！ ===");
        SpringApplication springApplication = new SpringApplication(WorkflowApplication.class);
        springApplication.addListeners(new StartingEventListener());
        springApplication.addListeners(new EnvPreparedListener());
        springApplication.run(args);
    }
}
