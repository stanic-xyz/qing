package cn.chenyunlong.qing.infrustructure.converter;

import cn.chenyunlong.qing.domain.attribute.WebControlType;
import jakarta.persistence.AttributeConverter;

public class WebControlTypeConverter implements AttributeConverter<WebControlType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(WebControlType webControlType) {
        return webControlType.getCode();
    }

    @Override
    public WebControlType convertToEntityAttribute(Integer code) {
        return WebControlType.of(code).orElse(null);
    }
}
