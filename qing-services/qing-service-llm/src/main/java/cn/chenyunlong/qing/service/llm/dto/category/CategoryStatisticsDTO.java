package cn.chenyunlong.qing.service.llm.dto.category;

import lombok.Data;

@Data
public class CategoryStatisticsDTO {
    private Long categoryId;
    private String categoryName;
    private String type;
    private Long transactionCount;
}
