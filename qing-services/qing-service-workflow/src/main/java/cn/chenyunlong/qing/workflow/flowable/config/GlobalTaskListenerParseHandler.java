package cn.chenyunlong.qing.workflow.flowable.config;

import cn.chenyunlong.qing.workflow.flowable.TaskAssignmentListener;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.*;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.parse.BpmnParseHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GlobalTaskListenerParseHandler implements BpmnParseHandler {

    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        return List.of(UserTask.class);
    }

    @Override
    public void parse(BpmnParse bpmnParse, BaseElement element) {
        if (!(element instanceof UserTask userTask)) {
            return;
        }

        // 创建 Assignment 事件的监听器
        FlowableListener assignmentListener = new FlowableListener();
        assignmentListener.setEvent(TaskListener.EVENTNAME_ASSIGNMENT);
        assignmentListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        assignmentListener.setImplementation("${taskAssignmentListener}");
        userTask.getTaskListeners().add(assignmentListener);

        // 创建 Create 事件的监听器（可选）
        FlowableListener createListener = new FlowableListener();
        createListener.setEvent(TaskListener.EVENTNAME_CREATE);
        createListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        createListener.setImplementation("${taskAssignmentListener}");
        userTask.getTaskListeners().add(createListener);

        // 创建 Complete 事件的监听器（可选）
        FlowableListener completeListener = new FlowableListener();
        completeListener.setEvent(TaskListener.EVENTNAME_COMPLETE);
        completeListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        completeListener.setImplementation("${taskAssignmentListener}");
        userTask.getTaskListeners().add(completeListener);
    }
}
