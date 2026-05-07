package cn.chenyunlong.qing.service.llm.dto.draft;

import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import lombok.Data;

@Data
public class ChangeDraftBatchStatusRequest {
    private DraftBatchStatusEnum targetStatus;
}
