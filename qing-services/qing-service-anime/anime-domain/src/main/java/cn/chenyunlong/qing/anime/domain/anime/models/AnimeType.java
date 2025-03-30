package cn.chenyunlong.qing.anime.domain.anime.models;

import cn.chenyunlong.qing.domain.common.AggregateId;

public record AnimeType(AggregateId aggregateId, String typeName) {
}
