package cn.chenyunlong.qing.domain.productcenter.template;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.annotation.TypeConverter;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import cn.chenyunlong.qing.domain.infrustructure.converter.TemplateTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "template")
public class Template extends BaseJpaAggregate {

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "描述")
    private String description;

    @FieldDesc(name = "编码")
    private String code;

    @FieldDesc(name = "分类ID")
    private Long categoryId;

    @Convert(converter = TemplateTypeConverter.class)
    @TypeConverter(toTypeFullName = "java.lang.Integer")
    private TemplateType templateType;

    /**
     * json map
     */
    @FieldDesc(name = "元数据")
    @Column(columnDefinition = "text")
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
