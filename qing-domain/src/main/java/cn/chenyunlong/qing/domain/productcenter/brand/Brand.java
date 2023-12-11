package cn.chenyunlong.qing.domain.productcenter.brand;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
@jakarta.persistence.Entity
@Table(name = "brand")
public class Brand extends BaseJpaAggregate {

    @NotEmpty(message = "品牌名称不能为空")
    @FieldDesc(name = "品牌名称")
    private String name;

    @FieldDesc(name = "品牌logo")
    private String logo;

    @FieldDesc(name = "品牌描述")
    private String description;

    @FieldDesc(name = "供应商")
    private String provider;


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
