package cn.chenyunlong.qing.workflow.listender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class EnvPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 此时可以使用日志框架（通常已初始化）
        log.info("=== 环境准备完成:ApplicationEnvironmentPreparedEvent:{} ===", event.getTimestamp());
    }
}
