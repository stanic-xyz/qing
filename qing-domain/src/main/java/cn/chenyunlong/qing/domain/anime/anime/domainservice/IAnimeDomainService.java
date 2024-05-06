package cn.chenyunlong.qing.domain.anime.anime.domainservice;

import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;

public interface IAnimeDomainService {

    /**
     * 创建动漫信息
     */
    Long createAnime(AnimeCreateRequest request);

}
