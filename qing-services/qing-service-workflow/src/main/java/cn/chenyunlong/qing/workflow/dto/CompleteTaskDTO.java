package cn.chenyunlong.qing.workflow.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CompleteTaskDTO {
    private String taskId;
    private String userId;
    private String approveResult; // PASS/REJECT
    private String comment;
    private Map<String, Object> variables;
}
