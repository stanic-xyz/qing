package cn.chenyunlong.qing.workflow.dto.message;

import lombok.Data;

@Data
public class TaskAssignmentMessage {

    private String event;
    private String taskId;
    private String taskName;
    private String assignee;
    private String processInstanceId;
    private String businessKey;
    // 其他需要的字段
}
