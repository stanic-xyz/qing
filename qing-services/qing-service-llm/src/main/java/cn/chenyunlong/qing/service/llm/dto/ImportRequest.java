package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImportRequest {
    private String uploadId;
    private Long accountId; // 新增：选择关联的账户ID
    private List<String> confirmedTempIds;
    private List<ModifiedRecord> modifications; // 用户修改过的记录

    @Data
    public static class ModifiedRecord {
        private String tempId;
        private String type; // EXPENSE, INCOME, TRANSFER
        private String merchant;
        private Long targetAccountId; // for TRANSFER
    }
}
