package cn.chenyunlong.qing.infrustructure.converter;

import cn.chenyunlong.qing.domain.productcenter.template.TemplateType;
import jakarta.persistence.AttributeConverter;

public class TemplateTypeConverter implements AttributeConverter<TemplateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TemplateType templateType) {
        return templateType.getValue();
    }

    @Override
    public TemplateType convertToEntityAttribute(Integer code) {
        return TemplateType.of(code).orElse(null);
    }
}
