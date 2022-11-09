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

package cn.chenyunlong.qing.service;

import cn.chenyunlong.qing.domain.bilibili.BilibiliAnimeScoreEntity;
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
