package cn.chenyunlong.qing.service.llm.enums;

import cn.chenyunlong.common.enums.CommonEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MERCHANT (商户), INDIVIDUAL (个人), CORPORATE (企业)
 */
@RequiredArgsConstructor
public enum CounterpartyTypeEnum implements CommonEnum {
    MERCHANT(1, "商户"),
    INDIVIDUAL(2, "个人"),
    CORPORATE(3, "企业");

    private final int code;

    @Getter
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
