package cn.chenyunlong.zhangli.service;

import cn.chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Stan
 */
public interface BilibiliAnimeService {
    /**
     * 同步哔哩哔哩的动漫信息
     */
    void updateAnimeInfo();

    /**
     * 获取B站动漫的评分记录
     *
     * @param animeId   动漫ID
     * @param startTime 开始时间
     * @param entTime   结束时间
     * @return 评分记录
     */
    List<BilibiliAnimeScoreEntity> getScoreInfoList(Long animeId, LocalDateTime startTime, LocalDateTime entTime);
}
