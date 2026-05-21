package cn.chenyunlong.qing.service.llm.dto.imports;

import cn.chenyunlong.qing.service.llm.enums.ImportModeEnum;
import lombok.Data;

@Data
public class QianjiImportRequest {
    private ImportModeEnum categoryMode = ImportModeEnum.SKIP;   // 分类冲突策略
    private ImportModeEnum accountMode = ImportModeEnum.SKIP;    // 账户冲突策略
    private boolean dryRun = false;     // 是否仅模拟执行
}
