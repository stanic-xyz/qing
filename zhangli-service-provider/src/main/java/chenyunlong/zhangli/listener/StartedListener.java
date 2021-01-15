package chenyunlong.zhangli.listener;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 启动成功监听器
 *
 * @author Stan
 * @date 2021.01.15
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ZhangliProperties zhangliProperties;

    public StartedListener(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        String blogUrl = zhangliProperties.getSecurity().getAnonUrl();
        log.info(AnsiOutput.toString(AnsiColor.GREEN, "Application started at " + event.getTimestamp()));
        log.info(AnsiOutput.toString(AnsiColor.GREEN, "uploadDir:" + zhangliProperties.getFile().getBaseUploadDir()));
        if (!zhangliProperties.getSwagger().isDocDisabled()) {
            log.info(AnsiOutput.toString(AnsiColor.GREEN, "Api doc enabled at:/swagger-ui.html"));
        } else {
            log.info(AnsiOutput.toString(AnsiColor.GREEN, "Swagger api page is disabled!"));
        }
        log.info(AnsiOutput.toString(AnsiColor.GREEN, "Application started successfully!"));
    }
}
