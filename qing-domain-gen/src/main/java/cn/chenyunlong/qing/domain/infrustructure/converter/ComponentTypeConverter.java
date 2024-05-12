package cn.chenyunlong.qing.domain.infrustructure.converter;


import cn.chenyunlong.qing.domain.productcenter.templateitem.ComponentType;
import jakarta.persistence.AttributeConverter;

public class ComponentTypeConverter implements AttributeConverter<ComponentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ComponentType componentType) {
        return componentType.getValue();
    }

    @Override
    public ComponentType convertToEntityAttribute(Integer code) {
        return ComponentType.of(code).orElse(null);
    }
}
