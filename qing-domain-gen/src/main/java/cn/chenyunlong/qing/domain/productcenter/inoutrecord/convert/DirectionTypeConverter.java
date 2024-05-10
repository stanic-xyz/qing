package cn.chenyunlong.qing.domain.productcenter.inoutrecord.convert;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import jakarta.persistence.AttributeConverter;

public class DirectionTypeConverter implements AttributeConverter<DirectionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DirectionType directionType) {
        return directionType.getValue();
    }

    @Override
    public DirectionType convertToEntityAttribute(Integer code) {
        return DirectionType.of(code).orElse(null);
    }
}
