package cn.chenyunlong.qing.leave.event;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // 打印事件信息
        log.info("当前流程创建了事件：「{}」,指定人：「{}」", delegateTask.getName(), delegateTask.getAssignee());
    }
}
