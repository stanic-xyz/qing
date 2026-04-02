package cn.chenyunlong.qing.service.llm.dto.ext;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class AlipayExtData implements BillExtData {
    // 收/付款方式(可能为空，为空的时候一般是收红包之类的，这个时候的账号可以匹配为余额宝账户）
    private String paymentMethod;
    // 商家订单号
    private String merchantOrderNo;
    // 对方账号（为空的时候为/）
    private String counterpartyAccount;
    // 交易对方名称 (如：鲜农果蔬）
    private String counterpartyName;
    //交易订单号
    private String tradeOrderNo;
    // 商品说明
    private String goodsDescription;

    public static AlipayExtData fromRaw(String originalData, ObjectMapper objectMapper) {
        if (originalData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(originalData, AlipayExtData.class);
        } catch (Exception e) {
            return null;
        }
    }
}
