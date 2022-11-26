/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.third.bilibili.service.impl;

import cn.chenyunlong.qing.domain.bilibili.BilibiliAnimeScoreEntity;
import cn.chenyunlong.qing.domain.bilibili.mapper.BilibiliAnimeScoreMapper;
import cn.chenyunlong.qing.domain.third.bilibili.service.BilibiliAnimeScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 *
 */
@Service
public class BiliBiliAnimeScoreServiceImpl extends ServiceImpl<BilibiliAnimeScoreMapper, BilibiliAnimeScoreEntity> implements BilibiliAnimeScoreService {

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
