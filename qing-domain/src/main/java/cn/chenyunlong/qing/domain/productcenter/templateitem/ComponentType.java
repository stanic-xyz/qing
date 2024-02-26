package cn.chenyunlong.qing.domain.productcenter.templateitem;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

public enum ComponentType implements BaseEnum<Integer> {
    SELECT(1, "select"),
    DATE(2, "date"),
    DATE_RANGE(3, "dateRange");

    private final Integer value;
    private final String name;

    ComponentType(Integer code, String name) {
        this.value = code;
        this.name = name;
    }

    public static Optional<ComponentType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, ComponentType.class));
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
