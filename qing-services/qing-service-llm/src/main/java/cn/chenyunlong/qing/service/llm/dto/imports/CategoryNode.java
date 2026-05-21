package cn.chenyunlong.qing.service.llm.dto.imports;

import cn.chenyunlong.qing.service.llm.dto.CategoryTreeDTO;
import lombok.Data;

import java.util.List;

// 分类树节点
@Data
public class CategoryNode {
    private String name;                // 分类名（如“餐饮”）
    private String type;                // INCOME/EXPENSE/TRANSFER
    private Boolean needToCreate;
    private CategoryTreeDTO matchedCategory;
    private List<CategoryNode> children;
}
