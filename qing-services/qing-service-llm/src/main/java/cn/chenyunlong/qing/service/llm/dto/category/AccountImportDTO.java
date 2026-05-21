package cn.chenyunlong.qing.service.llm.dto.category;

import lombok.Data;

@Data
public class AccountImportDTO {
    private Integer rowNum;           // Excel行号
    private String type;              // INCOME / EXPENSE
    private String categoryName;      // 分类名称
    private String parentName;        // 大类名称
    private String icon;              // 图标
}
