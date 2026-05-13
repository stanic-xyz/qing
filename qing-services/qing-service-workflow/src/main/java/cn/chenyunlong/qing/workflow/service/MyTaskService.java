package cn.chenyunlong.qing.workflow.service;

import cn.chenyunlong.qing.workflow.dto.task.*;

import java.util.List;

public interface MyTaskService {
    void transfer(TransferTaskDTO dto); // 转办

    void addSign(AddSignDTO dto);       // 加签

    void rejectToPrevious(RejectTaskDTO dto); // 驳回至上一节点

    void deleteTask(String taskId);     // 减签

    List<HistoryRecordVO> getHistory(String businessKey); // 审批历史

    ProcessImageVO getProcessImage(String processInstanceId); // 流程图高亮
}
