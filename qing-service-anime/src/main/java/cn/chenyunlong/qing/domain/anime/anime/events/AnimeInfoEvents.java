package cn.chenyunlong.qing.domain.anime.anime.events;

import cn.chenyunlong.qing.domain.anime.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.AnimeInfoBizInfo;

public interface AnimeInfoEvents {

    record AnimeInfoInEvent(AnimeInfo animeInfo, AnimeInfoBizInfo bizInfo) {
    }

    record AnimeInfoOutEvent(AnimeInfo animeInfo, AnimeInfoBizInfo bizInfo) {
    }

}
