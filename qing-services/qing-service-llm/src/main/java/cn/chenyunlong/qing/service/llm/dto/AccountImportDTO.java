package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.ProcessStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountImportDTO {

    private String accountName;     // 必填
    private AccountDTO account;
    private AccountType accountType;     // 必填 DEBIT/CREDIT/WALLET
    private String bankName;
    private String channelCode;
    private String cardNumber;
    private BigDecimal initialBalance;
    private AccountStatusEnum status;          // ACTIVE/CLOSED
    private String remark;

    // 以下字段用于预览界面
    private ProcessStatusEnum processStatus;   // VALID, INVALID, DUPLICATE_OVERWRITE, DUPLICATE_SKIP
    private String errorMsg;        // 错误信息

    // 如果是重复数据，对应的已存在的账户ID
    private Long existingAccountId;
}
