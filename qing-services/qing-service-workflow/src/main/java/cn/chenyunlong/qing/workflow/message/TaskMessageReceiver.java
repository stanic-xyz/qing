package cn.chenyunlong.qing.workflow.message;

import cn.chenyunlong.qing.workflow.config.MqConfig;
import cn.chenyunlong.qing.workflow.dto.message.TaskAssignmentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskMessageReceiver {

    @RabbitListener(queues = MqConfig.QUEUE, ackMode = "MANUAL")
    public void receiveTaskMessage(
            TaskAssignmentMessage message,   // 直接使用消息类
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) {
        try {
            log.info("📥 收到审批待办消息：{}", message);

            String assignee = message.getAssignee();
            String taskName = message.getTaskName();
            String businessKey = message.getBusinessKey();
            String event = message.getEvent();

            log.info("event = {},assignee = {},taskName = {},businessKey = {}", event, assignee, taskName, businessKey);

            // ==============================
            // 业务处理：推送待办（站内信/短信/钉钉等）
            // ==============================
            // 假设有一个推送服务
            // notificationService.sendToUser(assignee, taskName, businessKey);

            // 处理成功，手动 ACK
            channel.basicAck(deliveryTag, false);
            log.info("✅ 消息处理成功，已 ACK, deliveryTag={}", deliveryTag);
        } catch (Exception e) {
            log.error("❌ 消息处理失败，将拒绝并重新入队", e);
            try {
                // 拒绝当前消息，重新入队（可根据业务决定是否重试）
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("basicNack 失败", ex);
            }
        }
    }
}
