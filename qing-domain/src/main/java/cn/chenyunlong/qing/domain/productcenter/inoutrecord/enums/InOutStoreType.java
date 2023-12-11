package cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums;

import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum InOutStoreType implements BaseEnum<Integer> {
    IN_INIT(1, "初始入库"),
    IN_RETURN(2, "退货入库"),
    IN_TRANSFER(3, "调拨入库"),
    OUT_TRANSFER(4, "调拨出库"),
    OUT_RETURN(5, "退货出库"),
    OUT_PRODUCE(6, "生产出库"),
    IN_PRODUCE(7, "生产入库");

    private final Integer value;
    private final String name;

    private InOutStoreType(Integer code, String name) {
        this.value = code;
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static Optional<InOutStoreType> of(Integer code) {
        return Optional.ofNullable((InOutStoreType) BaseEnum.parseByCode(code, InOutStoreType.class));
    }
}