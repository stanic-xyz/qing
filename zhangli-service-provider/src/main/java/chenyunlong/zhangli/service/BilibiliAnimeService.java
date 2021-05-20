package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.bilibili.BiliAnime;

import java.util.List;

/**
 * @author Stan
 */
public interface BilibiliAnimeService {
    /**
     * 添加bilibili上面同步的视频
     *
     * @param animeList 动漫列表
     */
    void insertBatch(List<BiliAnime> animeList);
}
