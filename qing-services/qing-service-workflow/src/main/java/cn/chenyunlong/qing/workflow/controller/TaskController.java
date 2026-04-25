package cn.chenyunlong.qing.workflow.controller;

import cn.chenyunlong.qing.workflow.dto.CompleteTaskDTO;
import cn.chenyunlong.qing.workflow.dto.task.*;
import cn.chenyunlong.qing.workflow.dto.vo.TaskVO;
import cn.chenyunlong.qing.workflow.service.LeaveProcessService;
import cn.chenyunlong.qing.workflow.service.MyTaskService;
import cn.chenyunlong.qing.workflow.utils.Result;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService flowableTaskService;
    private final LeaveProcessService leaveProcessService;
    private final MyTaskService myTaskService;

    /**
     * 获取代办列表 (这里暂时查看所有的)
     **/
    @GetMapping
    public List<JSONObject> getTasks() {
        List<Task> taskList = flowableTaskService
                .createTaskQuery()
                //查询业务类型为指定的任务
                //.processInstanceBusinessKey("LEAVE")
                //查询所有zhangsan用户代办的任务
                //.taskAssignee(SysConstan.USER_ID)
                .processInstanceBusinessKey("LeaveProcessServiceTest_00001")
                .list();

        List<JSONObject> jsonObjects = new ArrayList<>();

        for (Task task : taskList) {
            JSONObject json = new JSONObject();
            json.put("taskId", task.getId());
            json.put("name", task.getName());
            json.put("assignee", task.getAssignee());
            json.put("processDefinitionId", task.getProcessDefinitionId());
            json.put("processInstanceId", task.getProcessInstanceId());

            // 获取与任务相关的所有变量
            Map<String, Object> taskVariables = flowableTaskService.getVariables(task.getId());

            // 打印出任务的变量
            json.putAll(taskVariables);

            jsonObjects.add(json);
        }

        return jsonObjects;
    }


    @PostMapping("/{taskId}/submit")
    public void submit(@PathVariable String taskId) {
        Task task = flowableTaskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee("123")
                .singleResult();
        if (task != null) {
            HashMap<String, Object> variables = new HashMap<>();
            variables.put("taskId", task.getId());
            flowableTaskService.complete(taskId, "123", variables);
        }
        leaveProcessService.completeSubmitTask(taskId);
    }

    @PostMapping("/complete")
    public Result<Void> completeTask(@RequestBody CompleteTaskDTO dto) {
        // 权限校验（自己加）
        Map<String, Object> variables = new HashMap<>();
        variables.put("approveResult", dto.getApproveResult());
        variables.put("approver", dto.getUserId());
        variables.put("comment", dto.getComment());

        variables.putAll(dto.getVariables());

        // 原生 API 封装
        flowableTaskService.complete(dto.getTaskId(), variables);
        return Result.success(null);
    }

    @GetMapping("/todo")
    public Result<List<TaskVO>> getMyTodo(String userId) {
        List<Task> tasks = flowableTaskService.createTaskQuery()
                .taskAssignee(userId)
                .active()
                .orderByTaskCreateTime()
                .desc()
                .list();

        List<TaskVO> result = tasks.stream().map(task -> {
            TaskVO vo = new TaskVO();
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setAssignee(task.getAssignee());
            return vo;
        }).collect(Collectors.toList());

        return Result.success(result);
    }

    // 1. 转办
    @PostMapping("/transfer")
    public Result<Void> transfer(@RequestBody TransferTaskDTO dto) {
        myTaskService.transfer(dto);
        return Result.success(null);
    }

    // 2. 加签
    @PostMapping("/addSign")
    public Result<Void> addSign(@RequestBody AddSignDTO dto) {
        myTaskService.addSign(dto);
        return Result.success(null);
    }

    // 3. 减签
    @PostMapping("/delete/{taskId}")
    public Result<Void> delete(@PathVariable String taskId) {
        myTaskService.deleteTask(taskId);
        return Result.success(null);
    }

    // 4. 驳回至上一节点
    @PostMapping("/reject")
    public Result<Void> reject(@RequestBody RejectTaskDTO dto) {
        myTaskService.rejectToPrevious(dto);
        return Result.success(null);
    }

    // 5. 审批历史记录
    @GetMapping("/history/{businessKey}")
    public Result<List<HistoryRecordVO>> history(@PathVariable String businessKey) {
        return Result.success(myTaskService.getHistory(businessKey));
    }

    // 6. 流程图高亮追踪
    @GetMapping("/image/{processInstanceId}")
    public Result<ProcessImageVO> image(@PathVariable String processInstanceId) {
        return Result.success(myTaskService.getProcessImage(processInstanceId));
    }

}
