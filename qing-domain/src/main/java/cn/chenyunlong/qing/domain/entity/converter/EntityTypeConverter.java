package cn.chenyunlong.qing.domain.entity.converter;

import cn.chenyunlong.qing.domain.entity.EntityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EntityTypeConverter implements AttributeConverter<EntityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EntityType directionType) {
        return directionType.getValue();
    }

    @Override
    public EntityType convertToEntityAttribute(Integer code) {
        return EntityType.of(code).orElse(null);
    }
}
