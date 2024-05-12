package cn.chenyunlong.qing.domain.attribute;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.qing.domain.infrustructure.converter.WebControlTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "attribute")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Attribute extends BaseJpaAggregate {

    @FieldDesc(name = "唯一编码")
    private String uniqueCode;

    @FieldDesc(name = "前端控件类型")
    @Convert(converter = WebControlTypeConverter.class)
    private WebControlType controlType;

    private ValidStatus validStatus;
}
