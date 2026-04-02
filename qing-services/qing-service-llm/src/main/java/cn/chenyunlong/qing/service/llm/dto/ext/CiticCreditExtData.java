package cn.chenyunlong.qing.service.llm.dto.ext;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CiticCreditExtData implements BillExtData {
    private String cardLast4;        // 卡号后四位
    private String billingCurrency;  // 结算币种
    private BigDecimal billingAmount;// 结算金额
}
