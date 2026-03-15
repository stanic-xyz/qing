package cn.chenyunlong.oss.leave;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LeaveProcessService {

    public static final String PROCESS_DEFINITION_KEY = "leaveApprovalProcess";
    public static final String SIGNAL_WITHDRAW = "Signal_WithdrawLeave";

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public LeaveProcessService(RuntimeService runtimeService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    public ProcessInstance startLeaveProcess(String applicant, int leaveDays, String reason) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", applicant);
        variables.put("leaveDays", leaveDays);
        variables.put("reason", reason);
        return runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
    }

    public Task querySingleTask(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }

    public void completeSubmitTask(String taskId) {
        taskService.complete(taskId);
    }

    public void completeSupervisorTask(String taskId, boolean approved) {
        taskService.complete(taskId, Map.of("supervisorApproved", approved));
    }

    public void completeHrTask(String taskId, boolean approved) {
        taskService.complete(taskId, Map.of("hrApproved", approved));
    }

    public void completeGmTask(String taskId, boolean approved) {
        taskService.complete(taskId, Map.of("gmApproved", approved));
    }

    public void completeReworkTask(String taskId) {
        taskService.complete(taskId);
    }

    public void withdraw(String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery()
            .processInstanceId(processInstanceId)
            .signalEventSubscriptionName(SIGNAL_WITHDRAW)
            .singleResult();
        if (execution != null) {
            runtimeService.signalEventReceived(SIGNAL_WITHDRAW, execution.getId());
        }
    }
}
