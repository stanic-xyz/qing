package cn.chenyunlong.qing.domain.anime.anime.models;

import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.Getter;

@Getter
public class AnimeId extends AggregateId {

    private Long id;

    public AnimeId(Long id) {
        this.id = id;
    }

}
