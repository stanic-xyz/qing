package cn.chenyunlong.qing.service.llm.dto.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryPreviewResult {
    private Integer totalCount;               // 总记录数
    private Integer validCount;               // 有效记录数
    private List<PreviewItem> previewItems;   // 明细列表

    @Data
    public static class PreviewItem {
        private Integer rowNum;
        private String type;
        private String categoryName;
        private String parentName;
        private String icon;
        private Boolean valid;          // 是否有效
        private String errorMsg;        // 错误信息
        private Boolean conflict;       // 是否存在冲突（已有同名分类）
        private Long conflictId;        // 冲突分类ID
        private String suggestedAction; // 建议操作: CREATE / UPDATE / SKIP
    }
}
