package cn.chenyunlong.qing.workflow.dto.task;

import lombok.Data;

import java.util.Date;
import java.util.List;

// 4. 历史记录 VO
@Data
public class HistoryRecordVO {
    private String taskName;
    private String approver;
    private String approveResult;
    private String comment;
    private Date createTime;
    private Date endTime;
}
