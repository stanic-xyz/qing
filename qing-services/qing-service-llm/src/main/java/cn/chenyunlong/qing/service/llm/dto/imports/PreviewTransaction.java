package cn.chenyunlong.qing.service.llm.dto.imports;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 交易预览项
@Data
public class PreviewTransaction {
    private LocalDateTime time;
    private String type;                // 支出/收入/转账/退款
    private BigDecimal amount;
    private String fromAccount;
    private String toAccount;
    private String category;            // 一级分类
    private String subCategory;         // 二级分类
    private String remark;
}
