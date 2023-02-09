package cn.chenyunlong.qing.domain.anime.anime.domainservice;


import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum InOutBizType implements BaseEnum {

    IN_FIRST(1, "初始入库"),
    OUT_TRANSFER(2, "调拨出库"),
    IN_TRANSFER(3, "调拨入库"),
    IN_BUY(4, "购买入库"),
    OUT_SALE(5, "销售出库");

    private final Integer code;
    private final String name;
    InOutBizType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<InOutBizType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(InOutBizType.class, code));
    }

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
