package cn.chenyunlong.qing.domain.productcenter.product;

import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum SerializeType implements BaseEnum<Integer> {
    SERIALIZE(1, "序列化"),
    UN_SERIALIZE(2, "非序列化");

    private final Integer code;
    private final String name;

    private SerializeType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static Optional<SerializeType> of(Integer code) {
        return Optional.ofNullable((SerializeType) BaseEnum.parseByCode(code, SerializeType.class));
    }
}