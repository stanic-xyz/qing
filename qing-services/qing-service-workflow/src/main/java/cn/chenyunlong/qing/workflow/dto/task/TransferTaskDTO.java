package cn.chenyunlong.qing.workflow.dto.task;

import lombok.Data;

import java.util.List;

// 1. 转办 DTO
@Data
public class TransferTaskDTO {
    private String taskId;
    private String sourceUserId;  // 原审批人
    private String targetUserId;  // 目标审批人
}
