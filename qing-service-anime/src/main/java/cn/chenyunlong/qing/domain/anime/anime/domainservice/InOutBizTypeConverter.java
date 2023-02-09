package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import javax.persistence.AttributeConverter;

public class InOutBizTypeConverter implements AttributeConverter<InOutBizType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InOutBizType inOutBizType) {
        return inOutBizType.getValue();
    }

    @Override
    public InOutBizType convertToEntityAttribute(Integer code) {
        return InOutBizType.of(code).orElse(null);
    }
}
