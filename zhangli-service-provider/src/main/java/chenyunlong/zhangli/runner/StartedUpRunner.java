package chenyunlong.zhangli.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Stan
 */
@Order
@Slf4j
@Component
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;

    public StartedUpRunner(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            log.info(" __    ___   _      ___   _     ____ _____  ____ ");
            log.info("/ /`  / / \\ | |\\/| | |_) | |   | |_   | |  | |_  ");
            log.info("\\_\\_, \\_\\_/ |_|  | |_|   |_|__ |_|__  |_|  |_|__ ");
            log.info("                                                      ");
            log.info("启动完毕，时间：" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }
}
