package cn.chenyunlong.qing.camunda.execution;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StartExecutionListener implements ExecutionListener {

    /**
     * 按照代码检查的要求，这里需要又注释说明这里如何实现.
     *
     * @param execution 执行
     */
    @Override
    public void notify(final DelegateExecution execution) {
        System.out.println("ExecutionListener--事件：【"
                               + execution.getEventName() + "】--触发了");
        if ("start".equals(execution.getEventName())) {
            List<String> userOneList = new ArrayList<>();
            userOneList.add("zhangsan");
            userOneList.add("lisi");
            execution.setVariable("userOneList", userOneList);
        }
    }
}
