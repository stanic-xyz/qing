package chenyunlong.zhangli.common.service;

import chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface BilibiliAnimeScoreService extends IService<BilibiliAnimeScoreEntity> {

    /**
     * 更新动漫的评分信息
     *
     * @param animeId 动漫ID
     * @param score   评分
     * @return 评分是否发生变化
     */
    boolean updateAnimeScore(Long animeId, Double score);
}
