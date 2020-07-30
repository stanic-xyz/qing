package chenyunlong.zhangli.listener;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private Logger log = LoggerFactory.getLogger((StartedListener.class));

    private final ZhangliProperties zhangliProperties;

    public StartedListener(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        this.printStartInfo();
    }

    private void printStartInfo() {
        String blogUrl = zhangliProperties.getSecurity().getAnonUrl();
        if (log.isInfoEnabled()) {
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Halo started at         ", blogUrl));
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "uploadDir is   ", blogUrl, "/", zhangliProperties.getFile().getBaseUploadDir()));
            if (!zhangliProperties.getSwagger().isDocDisabled()) {
                log.debug(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Halo api doc was enabled at  ", blogUrl, "/swagger-ui.html"));
            }
            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Zhangli has started successfully!"));
        }
    }
}
