package cn.chenyunlong.qing.domain.productcenter.template;

import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum TemplateType implements BaseEnum<Integer> {
    PRODUCT(1, "产品"),
    WORK_ORDER(2, "工作流"),
    QUESTION(3, "问卷");

    private final Integer value;
    private final String name;

    private TemplateType(Integer code, String name) {
        this.value = code;
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static Optional<TemplateType> of(Integer code) {
        return Optional.ofNullable((TemplateType) BaseEnum.parseByCode(code, TemplateType.class));
    }
}
