package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImportRequest {
    private String uploadId;
    private Long accountId; // 新增：选择关联的账户ID
    private List<String> confirmedTempIds;
}
