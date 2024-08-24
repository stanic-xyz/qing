package cn.chenyunlong.qing.domain.productcenter.goods.constant;

import cn.chenyunlong.common.constants.BaseEnum;
import lombok.Getter;

/**
 * 10 20 001 前两位 产品中心代表一个部门，一个领域  job  哪个应用
 */
@Getter
public enum ProductErrorCode implements BaseEnum<Integer> {

    PRODUCT_NAME_EMPTY(1001001, "商品名称不能为空"),
    GOODS_HAS_IN(1001002, "商品已经在库里"),
    GOODS_HAS_OUT(1001003, "商品已经出库");

    private final Integer value;
    private final String name;

    ProductErrorCode(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
}
