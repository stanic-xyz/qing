package cn.chenyunlong.qing.workflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveProcessService {

    public static final String PROCESS_DEFINITION_KEY = "Signal_WithdrawLeave";

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final IdentityService identityService;


    public ProcessInstance startLeaveProcess(String applicant, int leaveDays, String reason, String directSupervisor) {
        String businessKey = "LeaveProcessServiceTest_00001";

        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", applicant);
        variables.put("leaveDays", leaveDays);
        variables.put("reason", reason);
        variables.put("businessType", "业务类型");
        variables.put("directSupervisor", directSupervisor);

        identityService.setAuthenticatedUserId(applicant);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, businessKey, variables);
        log.info("流程实例id-{}", processInstance.getId());
        identityService.setAuthenticatedUserId(null);

        return processInstance;
    }

    public Task querySingleTask(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }

    public void completeSubmitTask(String taskId) {
        Task singleResult = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee("123")
                .singleResult();
        if (singleResult != null) {
            taskService.complete(singleResult.getId());
        }
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
                .signalEventSubscriptionName("Signal_WithdrawLeave")
                .singleResult();
        if (execution != null) {
            runtimeService.signalEventReceived("Signal_WithdrawLeave", execution.getId());
        }
    }
}
