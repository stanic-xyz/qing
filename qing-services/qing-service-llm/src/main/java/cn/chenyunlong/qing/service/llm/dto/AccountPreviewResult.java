package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;
import java.util.List;

@Data
public class AccountPreviewResult {
    private int totalCount;
    private int validCount;
    private int invalidCount;
    private int duplicateOverwriteCount;
    private int duplicateSkipCount;

    private List<AccountImportDTO> items;
}
