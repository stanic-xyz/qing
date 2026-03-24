package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImportRequest {
    private String uploadId;
    private List<String> confirmedTempIds;
}
