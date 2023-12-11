package cn.chenyunlong.qing.domain.productcenter.productsku;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenRepository
@GenService
@GenServiceImpl
@GenFeign(serverName = "stanic")
@GenController
@GenMapper
@Entity
@Table(name = "product_sku")
@Data
public class ProductSku extends BaseJpaAggregate {

    @FieldDesc(name = "产品ID")
    private Long productId;

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "编号")
    private String code;

    /**
     * 1: 苹果  2：
     */
    @FieldDesc(name = "sku信息")
    private String skuData;

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreCreator
    private ValidStatus validStatus;

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
