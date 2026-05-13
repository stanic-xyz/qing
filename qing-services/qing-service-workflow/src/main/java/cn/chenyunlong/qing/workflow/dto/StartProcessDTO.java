package cn.chenyunlong.qing.workflow.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StartProcessDTO {
    private String processKey;    // 流程KEY
    private String businessKey;   // 业务单号（核心）
    private String creator;       // 发起人
    private Map<String, Object> variables; // 表单变量
}
