package cn.chenyunlong.qing.service.llm.dto.account;

import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountUpdateDTO {
    private String accountName;
    private AccountType accountType;
    private String bankName;
    private Long channel;   // 接收字符串，如 "ALIPAY"
    private String icon;
    private String remark;
    private String cardNumber;
    private BigDecimal initialBalance; // 期初余额
    private BigDecimal currentBalance; // 当前余额

    private AccountStatusEnum status;
}
