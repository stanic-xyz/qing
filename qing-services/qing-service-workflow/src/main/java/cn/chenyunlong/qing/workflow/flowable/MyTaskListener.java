package cn.chenyunlong.qing.workflow.flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyTaskListener implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("当前流程创建了事件：「{}」,指定人：「{}」", execution.getEventName(), execution.getVariable("assignee"));
        execution.setVariable("hrApproved", true);
    }
}
