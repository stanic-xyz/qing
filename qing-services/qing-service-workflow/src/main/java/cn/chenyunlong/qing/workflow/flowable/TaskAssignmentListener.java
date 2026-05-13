package cn.chenyunlong.qing.workflow.flowable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskAssignmentListener implements TaskListener {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "bpm.task.exchange";
    private static final String ROUTING_KEY = "bpm.task.key";

    /**
     * 事件：
     * create = 创建待办
     * assignment = 分配给某人
     * complete = 完成审批
     * delete = 删除任务
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        String event = delegateTask.getEventName();
        String taskId = delegateTask.getId();
        String taskName = delegateTask.getName();
        String assignee = delegateTask.getAssignee(); // 被分配人
        String processInstanceId = delegateTask.getProcessInstanceId();
        String executionId = delegateTask.getExecutionId();
        String businessKey = delegateTask.getVariable("businessKey") != null ?
                delegateTask.getVariable("businessKey").toString() : null;

        // 构造消息体
        Map<String, Object> msg = new HashMap<>();
        msg.put("event", event);
        msg.put("taskId", taskId);
        msg.put("taskName", taskName);
        msg.put("assignee", assignee);
        msg.put("processInstanceId", processInstanceId);
        msg.put("executionId", executionId);
        msg.put("businessKey", businessKey);
        msg.put("variables", delegateTask.getVariables());
        msg.put("timestamp", System.currentTimeMillis());

        // 根据事件类型记录不同的日志
        switch (event) {
            case "create":
                log.info("✅ 流程任务已创建：{} → 任务ID: {}", taskName, taskId);
                break;
            case "assignment":
                log.info("✅ 流程任务已分配：{} → 用户: {}", taskName, assignee);
                break;
            case "complete":
                log.info("✅ 流程任务已完成：{} → 用户: {}", taskName, assignee);
                break;
            case "delete":
                log.info("✅ 流程任务已删除：{} → 任务ID: {}", taskName, taskId);
                break;
            default:
                log.info("✅ 流程任务事件：{} → 事件类型: {}", taskName, event);
                break;
        }

        // 发送到消息队列
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, msg);
        log.info("✅ 流程任务消息已发送到MQ：Exchange={}, RoutingKey={}, Event={}", EXCHANGE, ROUTING_KEY, event);
    }
}
