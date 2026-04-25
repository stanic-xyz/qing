package cn.chenyunlong.qing.workflow.flowable;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("sendNotificationListener")
public class SendNotificationListener implements ExecutionListener {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("流程执行事件触发，当前流程实例ID: " + execution.getProcessInstanceId());
        rabbitTemplate.send("flowable.event.execution", new Message(StrUtil.format("{},{}", execution.getProcessInstanceId(), execution.getCurrentActivityId()).getBytes()));
        // 在这里注入你的业务服务，比如发送邮件的Service
    }
}
