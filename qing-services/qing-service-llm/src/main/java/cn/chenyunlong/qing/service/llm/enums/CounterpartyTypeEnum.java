package cn.chenyunlong.qing.service.llm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MERCHANT (商户), INDIVIDUAL (个人), CORPORATE (企业)
 */
public enum CounterpartyTypeEnum {
    MERCHANT("商户"),
    INDIVIDUAL("个人"),
    CORPORATE("企业");

    @Getter
    private final String description;

    CounterpartyTypeEnum(String description) {
        this.description = description;
    }
}
