package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

@Data
public class ImportResult {

    private int totalRecords;
    private int importedCategories;
    private int importedAccounts;
    private int importedTransactions;
}
