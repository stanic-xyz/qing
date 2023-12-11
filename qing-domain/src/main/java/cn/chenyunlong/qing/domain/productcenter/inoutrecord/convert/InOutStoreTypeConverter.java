package cn.chenyunlong.qing.domain.productcenter.inoutrecord.convert;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import jakarta.persistence.AttributeConverter;

public class InOutStoreTypeConverter implements AttributeConverter<InOutStoreType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InOutStoreType inOutStoreType) {
        return inOutStoreType.getValue();
    }

    @Override
    public InOutStoreType convertToEntityAttribute(Integer code) {
        return InOutStoreType.of(code).orElse(null);
    }
}
