package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.Getter;

@Getter
public class AnimeId extends AggregateId {

    private final Long id;

    public AnimeId(Long id) {
        this.id = id;
    }

}
