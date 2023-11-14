package cn.chenyunlong.qing.infrustructure.converter;

import cn.chenyunlong.qing.domain.anime.PlayStatus;
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
