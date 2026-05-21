package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

import java.util.List;

@Data
public class QianjiPreviewResult {
    private Integer totalRecords;               // 总记录数
    private List<CategoryNode> categories;      // 分类树结构
    private List<String> accounts;              // 账户列表
    private List<PreviewTransaction> sampleTransactions; // 前10条记录样例
    private List<QianjiRecordDTO> records; // 全量解析结果（用于前端二次确认后直接提交执行，避免重复解析）
}
