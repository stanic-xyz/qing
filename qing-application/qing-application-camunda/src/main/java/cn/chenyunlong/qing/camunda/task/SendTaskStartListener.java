package cn.chenyunlong.qing.camunda.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component("sendTaskStartListener")
@RequiredArgsConstructor
public class SendTaskStartListener implements TaskListener {

    @Override
    public void notify(final DelegateTask task) {
        System.out.println("ExecutionListener--事件：【" + task.getName() + "】--触发了");
        task.setVariable("checkResult", 1);
        task.setAssignee("admin");
    }
}
