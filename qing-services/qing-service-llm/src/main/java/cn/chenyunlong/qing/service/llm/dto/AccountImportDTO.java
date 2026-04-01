package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountImportDTO {
    private String accountName;     // 必填
    private String accountType;     // 必填 DEBIT/CREDIT/WALLET
    private String bankName;
    private String channel;
    private String cardNumber;
    private BigDecimal initialBalance;
    private String status;          // ACTIVE/CLOSED
    private String remark;

    // 以下字段用于预览界面
    private String processStatus;   // VALID, INVALID, DUPLICATE_OVERWRITE, DUPLICATE_SKIP
    private String errorMsg;        // 错误信息
    
    // 如果是重复数据，对应的已存在的账户ID
    private Long existingAccountId; 
}
