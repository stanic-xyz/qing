package cn.chenyunlong.qing.domain.productcenter.goods;

import cn.chenyunlong.codegen.annotation.IgnoreCreator;
import cn.chenyunlong.codegen.annotation.IgnoreUpdater;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import cn.chenyunlong.qing.domain.productcenter.goods.constant.ProductErrorCode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand")
public class Goods extends BaseJpaAggregate {

    private String name;

    @FieldDesc(name = "规格ID")
    private Long skuId;

    @FieldDesc(name = "唯一编码")
    private String uniqueCode;

    @FieldDesc(name = "价格")
    private BigDecimal price;

    @FieldDesc(name = "批次号")
    private String batchNo;

    @FieldDesc(name = "仓库ID")
    private Long storeId;

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreCreator
    private ValidStatus validStatus;


    private void in() {
        if (!Objects.equals(getValidStatus(), ValidStatus.INVALID)) {
            throw new BusinessException(ProductErrorCode.GOODS_HAS_IN);
        }
        setValidStatus(ValidStatus.VALID);
    }

    private void out() {
        if (Objects.equals(getValidStatus(), ValidStatus.VALID)) {
            throw new BusinessException(ProductErrorCode.GOODS_HAS_OUT);
        }
        setValidStatus(ValidStatus.INVALID);
    }

    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }
}
