package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

import java.util.List;

@Data
public class QianjiExecuteRecordsRequest {
    private String sourceFile;
    private QianjiImportRequest request;
    private List<QianjiRecordDTO> records;

    private List<CategoryNode> categories;      // 分类树结构
    private List<String> accounts;              // 账户列表
}
