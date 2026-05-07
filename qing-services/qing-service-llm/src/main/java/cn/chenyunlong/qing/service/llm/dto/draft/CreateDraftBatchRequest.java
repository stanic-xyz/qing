package cn.chenyunlong.qing.service.llm.dto.draft;

import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import lombok.Data;

@Data
public class CreateDraftBatchRequest {
    private AdapterTypeEnum adapterType = AdapterTypeEnum.PARSER;
    private Integer totalRecords = 0;
}
