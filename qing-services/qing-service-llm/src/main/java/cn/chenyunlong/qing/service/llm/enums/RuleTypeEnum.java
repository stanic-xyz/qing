package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RuleTypeEnum implements CommonEnum {
    CHANNEL(1, "渠道匹配"),
    MERCHANT(2, "商户匹配"),
    COUNTERPARTY(3, "对手账户匹配"),
    INTERNAL_TRANSFER(4, "内部转账匹配"),
    CUSTOM(5, "自定义通用匹配");

    private final int code;
    private final String description;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
