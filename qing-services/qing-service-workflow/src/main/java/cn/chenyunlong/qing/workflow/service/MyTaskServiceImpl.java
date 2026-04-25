package cn.chenyunlong.qing.workflow.service;

import cn.chenyunlong.qing.workflow.dto.task.*;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.*;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MyTaskServiceImpl implements MyTaskService {

    private final TaskService taskService;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

    // ====================== 1. 转办 ======================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferTaskDTO dto) {
        Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();
        if (task == null) throw new RuntimeException("任务不存在");
        if (!dto.getSourceUserId().equals(task.getAssignee())) {
            throw new RuntimeException("只能转办自己的任务");
        }
        // 转办核心
        taskService.setAssignee(dto.getTaskId(), dto.getTargetUserId());
    }

    // ====================== 2. 加签 ======================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSign(AddSignDTO dto) {
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        // 克隆新任务（加签）
        Task newTask = taskService.newTask();
        newTask.setName(task.getName() + "-加签");
        newTask.setAssignee(dto.getAddUserId());
        newTask.setParentTaskId(dto.getTaskId());
        taskService.saveTask(newTask);
    }

    // ====================== 3. 减签 ======================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(String taskId) {
        taskService.deleteTask(taskId, "减签");
    }

    // ====================== 4. 驳回至上一节点 ======================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectToPrevious(RejectTaskDTO dto) {
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
        if (task == null) throw new RuntimeException("任务不存在");

        // 获取上一个用户任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        if (list.isEmpty()) throw new RuntimeException("无上一节点可驳回");

        String lastTaskId = list.getFirst().getId();
        // 跳转回上一节点
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(dto.getTaskId(), lastTaskId)
                .changeState();

        // 记录审批意见
        Map<String, Object> vars = new HashMap<>();
        vars.put("approveResult", "REJECT");
        vars.put("comment", dto.getComment());
        taskService.complete(dto.getTaskId(), vars);
    }

    // ====================== 5. 审批历史记录 ======================
    @Override
    public List<HistoryRecordVO> getHistory(String businessKey) {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        return list.stream().map(taskInstance -> {
            HistoryRecordVO vo = new HistoryRecordVO();
            vo.setTaskName(taskInstance.getName());
            vo.setApprover(taskInstance.getAssignee());
            vo.setComment((String) taskInstance.getTaskLocalVariables().get("comment"));
            vo.setApproveResult((String) taskInstance.getTaskLocalVariables().get("approveResult"));
            vo.setCreateTime(taskInstance.getCreateTime());
            vo.setEndTime(taskInstance.getEndTime());
            return vo;
        }).collect(Collectors.toList());
    }

    // ====================== 6. 流程图高亮追踪 ======================
    @Override
    public ProcessImageVO getProcessImage(String processInstanceId) {
        return null;
    }
}
