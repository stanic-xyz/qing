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

    /**
     * 按照代码检查的要求，这里需要又注释说明这里如何实现.
     * 注释说明：
     * 这里需要实现一个接口，接口的名称是固定的，不能修改，不能删除.
     *
     * @param task 执行任务
     */
    @Override
    public void notify(final DelegateTask task) {
        System.out.println("ExecutionListener--事件：【"
            + task.getName() + "】--触发了");
        task.setVariable("checkResult", 1);
        task.setAssignee("admin");
    }
}
