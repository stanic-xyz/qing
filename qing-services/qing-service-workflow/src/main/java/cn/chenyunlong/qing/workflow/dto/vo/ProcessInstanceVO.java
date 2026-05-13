package cn.chenyunlong.qing.workflow.dto.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProcessInstanceVO {
    private String processInstanceId;
    private String businessKey;
    private String processName;
    private List<TaskVO> taskList;
}
