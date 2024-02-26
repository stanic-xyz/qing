package cn.chenyunlong.qing.camunda.execution;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StartExecutionListener implements ExecutionListener {
    @Override
    public final void notify(final DelegateExecution execution) throws Exception {
        System.out.println("ExecutionListener--事件：【" + execution.getEventName() + "】--触发了");
        if ("start".equals(execution.getEventName())) {
            List<String> userOneList = new ArrayList<>();
            userOneList.add("zhangsan");
            userOneList.add("lisi");
            execution.setVariable("userOneList", userOneList);
        }
    }
}
