package cn.chenyunlong.qing.camunda.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component("sendTaskStartListener")
@RequiredArgsConstructor
public class SendTaskStartListener implements TaskListener {

    private final TaskService taskService;

    @Override
    public void notify(DelegateTask task) {
        System.out.println("ExecutionListener--事件：【" + task.getName() + "】--触发了");
        String taskDefinitionKey = task.getTaskDefinitionKey();
        task.setVariable("checkResult", 1);
        task.setAssignee("admin");
    }
}
