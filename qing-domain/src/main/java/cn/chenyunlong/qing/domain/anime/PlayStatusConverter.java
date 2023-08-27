package cn.chenyunlong.qing.domain.anime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PlayStatusConverter implements AttributeConverter<PlayStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PlayStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public PlayStatus convertToEntityAttribute(Integer dbData) {
        return PlayStatus.of(dbData).orElse(null);
    }
}
