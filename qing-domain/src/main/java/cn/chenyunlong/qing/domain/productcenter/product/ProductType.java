package cn.chenyunlong.qing.domain.productcenter.product;

import cn.chenyunlong.common.constants.BaseEnum;
import java.util.Optional;

public enum ProductType implements BaseEnum<Integer> {

    SINGLE(1, "单品"),
    KIT(2, "套件");

    private final Integer code;
    private final String name;

    ProductType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ProductType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, ProductType.class));
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
