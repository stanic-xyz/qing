package cn.chenyunlong.qing.workflow.dto.task;

import lombok.Data;

import java.util.List;

// 2. 加签 DTO
@Data
public class AddSignDTO {
    private String taskId;
    private String currentUserId;
    private String addUserId;     // 加签人
    private String type;         // BEFORE/AFTER 前加签/后加签
}
