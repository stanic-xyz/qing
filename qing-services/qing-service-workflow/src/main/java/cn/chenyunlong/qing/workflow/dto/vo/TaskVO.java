package cn.chenyunlong.qing.workflow.dto.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TaskVO {
    private String taskId;
    private String taskName;
    private String processInstanceId;
    private String assignee;
    private Date createTime;
}
