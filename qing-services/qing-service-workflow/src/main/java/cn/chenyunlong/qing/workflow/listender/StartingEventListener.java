package cn.chenyunlong.qing.workflow.listender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class StartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        // 此时日志系统可能还未完全初始化，建议使用 System.out
        System.out.println("=== ApplicationStartingEvent 触发，启动开始 ===:" + event.getTimestamp());
    }
}
