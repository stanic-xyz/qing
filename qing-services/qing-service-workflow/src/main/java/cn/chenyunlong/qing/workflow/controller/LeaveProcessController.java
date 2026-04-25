package cn.chenyunlong.qing.workflow.controller;

import cn.chenyunlong.qing.workflow.model.StartLeaveRequest;
import cn.chenyunlong.qing.workflow.model.TaskApproveRequest;
import cn.chenyunlong.qing.workflow.service.LeaveProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("leave")
public class LeaveProcessController {

    private final LeaveProcessService leaveProcessService;

    public LeaveProcessController(LeaveProcessService leaveProcessService) {
        this.leaveProcessService = leaveProcessService;
    }

    @PostMapping("/start")
    public Map<String, String> start(@RequestBody StartLeaveRequest request) {
        ProcessInstance processInstance = leaveProcessService.startLeaveProcess(request.applicant(), request.leaveDays(), request.reason(), request.directSupervisor());
        return Map.of("processInstanceId", processInstance.getProcessInstanceId());
    }

    @PostMapping("/{taskId}/supervisor")
    public void supervisorApprove(@PathVariable String taskId, @RequestBody TaskApproveRequest request) {
        leaveProcessService.completeSupervisorTask(taskId, request.approved());
    }

    @PostMapping("/{taskId}/hr")
    public void hrApprove(@PathVariable String taskId, @RequestBody TaskApproveRequest request) {
        leaveProcessService.completeHrTask(taskId, request.approved());
    }

    @PostMapping("/{taskId}/gm")
    public void gmApprove(@PathVariable String taskId, @RequestBody TaskApproveRequest request) {
        leaveProcessService.completeGmTask(taskId, request.approved());
    }

    @PostMapping("/{taskId}/rework")
    public void rework(@PathVariable String taskId) {
        leaveProcessService.completeReworkTask(taskId);
    }

    @PostMapping("/{processInstanceId}/withdraw")
    public void withdraw(@PathVariable String processInstanceId) {
        leaveProcessService.withdraw(processInstanceId);
    }
}
