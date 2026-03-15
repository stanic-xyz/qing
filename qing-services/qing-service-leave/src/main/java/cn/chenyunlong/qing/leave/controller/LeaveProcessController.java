package cn.chenyunlong.qing.leave.controller;

import cn.chenyunlong.qing.leave.model.StartLeaveRequest;
import cn.chenyunlong.qing.leave.model.TaskApproveRequest;
import cn.chenyunlong.qing.leave.service.LeaveProcessService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveProcessController {

    private final LeaveProcessService leaveProcessService;

    public LeaveProcessController(LeaveProcessService leaveProcessService) {
        this.leaveProcessService = leaveProcessService;
    }

    @PostMapping("/start")
    public Map<String, String> start(@RequestBody StartLeaveRequest request) {
        ProcessInstance processInstance = leaveProcessService.startLeaveProcess(request.applicant(), request.leaveDays(), request.reason());
        Task submitTask = leaveProcessService.querySingleTask(processInstance.getProcessInstanceId());
        return Map.of("processInstanceId", processInstance.getProcessInstanceId(), "currentTaskId", submitTask.getId());
    }

    @PostMapping("/{taskId}/submit")
    public void submit(@PathVariable String taskId) {
        leaveProcessService.completeSubmitTask(taskId);
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
