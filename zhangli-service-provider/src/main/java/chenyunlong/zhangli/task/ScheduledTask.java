package chenyunlong.zhangli.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    /**
     * 同步上网记录
     * (启动时同步一次，之后每两小时同步一次基础平台的记录）
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void syncNetworkLogs() {
        new Thread(() -> {
            log.info(new Date() + "同步学生上网记录");
        }).start();
    }
}
