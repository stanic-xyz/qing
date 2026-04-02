package cn.chenyunlong.qing.service.llm.dto.ext;

import lombok.Data;

@Data
public class JingdongExtData implements BillExtData {
    private String paymentMethod;       // 收/付款方式
    private String merchantOrderNo;     // 商家订单号
}
