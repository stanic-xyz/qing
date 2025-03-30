package cn.chenyunlong.qing.anime.infrastructure.converter;

import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import cn.chenyunlong.qing.infrastructure.converter.DomainEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class AnimeConverter implements DomainEntityConverter<Anime, AnimeEntity> {

    @Override
    public AnimeEntity toEntity(Anime anime) {
        AnimeEntity entity = new AnimeEntity();
        return entity;
    }

    @Override
    public Anime toDomain(AnimeEntity entity) {
        Anime anime = new Anime();


        return anime;
    }

}
