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

import cn.chenyunlong.qing.model.entities.bilibili.BilibiliAnimeScoreEntity;

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
