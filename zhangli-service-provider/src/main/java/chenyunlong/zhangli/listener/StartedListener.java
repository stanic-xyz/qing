package chenyunlong.zhangli.listener;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import lombok.extern.slf4j.Slf4j;
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

    public StartedListener(ZhangliProperties zhangliProperties) {
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
    }
}
