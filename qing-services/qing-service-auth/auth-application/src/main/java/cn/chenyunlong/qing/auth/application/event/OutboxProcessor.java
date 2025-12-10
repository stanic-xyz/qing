package cn.chenyunlong.qing.auth.application.event;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Outbox 处理器（扫描->发布->标记）
 */
@Service
public class OutboxProcessor {

    /**
     * 周期任务：处理未投递事件
     */
    @Transactional
    public void processUnsentEvents() {
        // 1. 查询 processed=false 的记录
        // 2. 将 payload 反序列化为 DomainEvent
        // 3. 发布到消息通道（event bus/kafka/rocketmq）
        // 4. 标记 processed=true


    }
}
