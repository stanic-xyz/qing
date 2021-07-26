package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.BilibiliAnimeScoreMapper;
import chenyunlong.zhangli.model.entities.bilibili.BilibiliAnimeScoreEntity;
import chenyunlong.zhangli.service.BilibiliAnimeScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 *
 */
@Service
public class BilibiliAnimeScoreServiceImpl extends ServiceImpl<BilibiliAnimeScoreMapper, BilibiliAnimeScoreEntity> implements BilibiliAnimeScoreService {

    @Override
    public boolean updateAnimeScore(Long animeId, Double score) {

        BilibiliAnimeScoreEntity entity = lambdaQuery()
                .eq(BilibiliAnimeScoreEntity::getAnimeId, animeId)
                .orderByDesc(BilibiliAnimeScoreEntity::getRecordTime)
                .last("limit 1")
                .list().stream().findFirst().orElse(null);
        //评分有变化的时候才记录
        if (entity == null || !entity.getScore().equals(score)) {
            BilibiliAnimeScoreEntity scoreEntity = new BilibiliAnimeScoreEntity();
            scoreEntity.setScore(score);
            scoreEntity.setAnimeId(animeId);
            scoreEntity.setRecordTime(LocalDateTime.now());
            scoreEntity.preCheck();
            save(scoreEntity);
            return true;
        } else {
            //更新最后修改时间
            entity.setUpdateTime(LocalDateTime.now());
            updateById(entity);
        }
        return false;
    }
}
