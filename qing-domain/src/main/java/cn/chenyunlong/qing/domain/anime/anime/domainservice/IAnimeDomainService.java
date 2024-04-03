package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;

public interface IAnimeDomainService {

    Long createAnime(AnimeCreateRequest request);
}
