/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.anime.domain.favorite.repository;

import cn.chenyunlong.qing.anime.domain.favorite.Favorite;

import java.util.List;
import java.util.Optional;

/**
 * 收藏夹仓储接口
 *
 * @author 陈云龙
 */
public interface FavoriteRepository {

    /**
     * 保存收藏记录
     */
    Favorite save(Favorite favorite);

    /**
     * 根据用户ID和动漫ID查找收藏记录
     */
    Optional<Favorite> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据用户ID查找所有收藏记录
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * 根据用户ID和收藏分组查找收藏记录
     */
    List<Favorite> findByUserIdAndFavoriteGroup(Long userId, String favoriteGroup);

    /**
     * 根据用户ID查找收藏分组列表
     */
    List<String> findFavoriteGroupsByUserId(Long userId);

    /**
     * 根据动漫ID统计收藏次数
     */
    Long countByAnimeId(Long animeId);

    /**
     * 检查用户是否收藏了某个动漫
     */
    Boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 删除收藏记录
     */
    void deleteById(Long id);

    /**
     * 根据用户ID和动漫ID删除收藏记录
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}
