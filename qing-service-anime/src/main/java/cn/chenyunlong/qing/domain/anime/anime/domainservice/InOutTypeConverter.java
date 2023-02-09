package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import javax.persistence.AttributeConverter;

public class InOutTypeConverter implements AttributeConverter<InOutType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InOutType inOutType) {
        return inOutType.getValue();
    }

    @Override
    public InOutType convertToEntityAttribute(Integer code) {
        return InOutType.of(code).orElse(null);
    }
}
