package cn.chenyunlong.qing.domain.anime.anime.exception;


import cn.chenyunlong.qing.domain.common.AggregateId;

public class AnimeNotFoundException extends RuntimeException {

    public AnimeNotFoundException(AggregateId aggregateId) {
        super("动漫未找到: " + aggregateId);
    }
}
