package cn.chenyunlong.qing.domain.infrustructure.converter;


import cn.chenyunlong.qing.domain.productcenter.product.SerializeType;
import jakarta.persistence.AttributeConverter;

public class SerializeTypeConverter implements AttributeConverter<SerializeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SerializeType serializeType) {
        return serializeType.getValue();
    }

    @Override
    public SerializeType convertToEntityAttribute(Integer code) {
        return SerializeType.of(code).orElse(null);
    }
}
