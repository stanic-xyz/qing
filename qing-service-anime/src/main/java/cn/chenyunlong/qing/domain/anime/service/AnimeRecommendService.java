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

package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.qing.domain.anime.AnimeRecommendEntity;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeRecommendParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeRecommendService {
    /**
     * 分页获取推荐信息
     *
     * @param pageable 分页信息
     * @return 分页
     */
    List<AnimeRecommendEntity> getRecommendAnimeInfoList(Pageable pageable, AnimeInfoQuery animeInfoQuery);

    /**
     * 获取推荐详情信息
     *
     * @param recommendId 推荐ID
     * @return 推荐信息
     */
    AnimeRecommendEntity getById(Long recommendId);

    /**
     * 添加推荐信息
     *
     * @param recommendEntity 推荐信息
     * @return 推荐信息
     */
    AnimeRecommendEntity addRecommend(AnimeRecommendEntity recommendEntity);

    /**
     * 添加推荐信息
     *
     * @param typeParam 推荐信息
     * @param pageable  分页信息
     * @return 推荐信息分页
     */
    IPage<AnimeRecommendEntity> pageBy(AnimeRecommendParam typeParam, Pageable pageable);

    AnimeRecommendEntity update(AnimeRecommendEntity animeRecommendEntity);
}
