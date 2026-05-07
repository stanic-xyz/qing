package cn.chenyunlong.qing.service.llm.dto.draft;

import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DraftBatchResponse {
    private Long id;
    private String batchNo;
    private AdapterTypeEnum adapterType;
    private DraftBatchStatusEnum status;
    private Integer progress;
    private Integer totalRecords;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> allowedActions;
}
