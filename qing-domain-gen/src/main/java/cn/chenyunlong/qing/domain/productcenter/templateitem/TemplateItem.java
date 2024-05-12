package cn.chenyunlong.qing.domain.productcenter.templateitem;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import cn.chenyunlong.qing.domain.infrustructure.converter.ComponentTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
public class TemplateItem extends BaseJpaAggregate {

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "编码")
    private String code;

    @FieldDesc(name = "默认值")
    private String defaultValue;

    @FieldDesc(name = "占位符")
    private String placeholder;

    @FieldDesc(name = "创建人")
    private String createUser;

    @FieldDesc(name = "排序号")
    private BigDecimal sortNum;

    @FieldDesc(name = "数据的请求url")
    private String dataUrl;

    @FieldDesc(name = "组件类型")
    @Convert(converter = ComponentTypeConverter.class)
    private ComponentType componentType;

    @FieldDesc(name = "描述")
    private String description;

    @FieldDesc(name = "元数据")
    private String metaData;

    @Convert(converter = ValidStatusConverter.class)
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
