package cn.chenyunlong.qing.workflow.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通知服务任务委托类
 * 演示如何在流程中集成自定义业务逻辑
 */
@Slf4j
@Component("notificationDelegate")
public class NotificationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // 从流程变量中获取业务数据
        Map<String, Object> variables = execution.getVariables();
        String processInstanceId = execution.getProcessInstanceId();
        String activityId = execution.getCurrentActivityId();

        log.info("===== 发送审批结果通知 =====");
        log.info("流程实例ID: {}", processInstanceId);
        log.info("当前活动ID: {}", activityId);
        log.info("流程变量: {}", variables);

        // 模拟发送通知的业务逻辑
        // 在实际项目中，这里可以调用邮件服务、短信服务或消息队列
        String applicant = (String) variables.getOrDefault("applicant", "未知申请人");
        Object days = variables.getOrDefault("days", 0);
        Object approvalResult = variables.getOrDefault("approvalResult", "PENDING");

        String message = String.format("亲爱的%s，您的%s天请假申请审批结果为：%s。",
                applicant, days, approvalResult);
        log.info("通知内容: {}", message);

        // 可以将通知结果存回流程变量
        execution.setVariable("notificationSent", true);
        execution.setVariable("notificationMessage", message);

        log.info("通知发送完成。");
    }
}
