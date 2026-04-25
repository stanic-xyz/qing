package cn.chenyunlong.qing.workflow.dto.task;

import lombok.Data;

import java.util.List;

// 3. 驳回 DTO
@Data
public class RejectTaskDTO {
    private String taskId;
    private String userId;
    private String comment;
}
