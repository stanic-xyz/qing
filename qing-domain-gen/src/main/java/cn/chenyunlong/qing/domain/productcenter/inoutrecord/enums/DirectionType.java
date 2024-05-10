package cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

public enum DirectionType implements BaseEnum<Integer> {
    IN(1, "入"), OUT(2, "出");

    private final Integer value;
    private final String name;

    DirectionType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Optional<DirectionType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, DirectionType.class));
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
