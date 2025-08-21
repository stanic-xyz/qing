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

package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.favorite.Favorite;
import cn.chenyunlong.qing.anime.domain.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 收藏夹应用服务
 *
 * @author 陈云龙
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * 添加收藏
     */
    public Favorite addToFavorite(Long userId, Long animeId, String favoriteGroup, 
                                String remark, Boolean isPublic) {
        // 检查是否已收藏
        if (favoriteRepository.existsByUserIdAndAnimeId(userId, animeId)) {
            throw new IllegalStateException("该动漫已在收藏夹中");
        }
        
        Favorite favorite = Favorite.create(userId, animeId, favoriteGroup, remark, isPublic);
        Favorite saved = favoriteRepository.save(favorite);
        log.info("用户 {} 收藏动漫 {}", userId, animeId);
        return saved;
    }

    /**
     * 取消收藏
     */
    public void removeFromFavorite(Long userId, Long animeId) {
        favoriteRepository.deleteByUserIdAndAnimeId(userId, animeId);
        log.info("用户 {} 取消收藏动漫 {}", userId, animeId);
    }

    /**
     * 更新收藏分组
     */
    public void updateFavoriteGroup(Long userId, Long animeId, String favoriteGroup) {
        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserIdAndAnimeId(userId, animeId);
        if (favoriteOpt.isPresent()) {
            Favorite favorite = favoriteOpt.get();
            favorite.updateFavoriteGroup(favoriteGroup);
            favoriteRepository.save(favorite);
            log.info("用户 {} 更新动漫 {} 的收藏分组为 {}", userId, animeId, favoriteGroup);
        } else {
            throw new IllegalStateException("收藏记录不存在");
        }
    }

    /**
     * 更新收藏备注
     */
    public void updateFavoriteRemark(Long userId, Long animeId, String remark) {
        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserIdAndAnimeId(userId, animeId);
        if (favoriteOpt.isPresent()) {
            Favorite favorite = favoriteOpt.get();
            favorite.updateRemark(remark);
            favoriteRepository.save(favorite);
            log.info("用户 {} 更新动漫 {} 的收藏备注", userId, animeId);
        } else {
            throw new IllegalStateException("收藏记录不存在");
        }
    }

    /**
     * 设置收藏公开状态
     */
    public void setFavoritePublicStatus(Long userId, Long animeId, Boolean isPublic) {
        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserIdAndAnimeId(userId, animeId);
        if (favoriteOpt.isPresent()) {
            Favorite favorite = favoriteOpt.get();
            favorite.setPublicStatus(isPublic);
            favoriteRepository.save(favorite);
            log.info("用户 {} 设置动漫 {} 的收藏公开状态为 {}", userId, animeId, isPublic);
        } else {
            throw new IllegalStateException("收藏记录不存在");
        }
    }

    /**
     * 获取用户收藏列表
     */
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    /**
     * 根据分组获取用户收藏列表
     */
    public List<Favorite> getUserFavoritesByGroup(Long userId, String favoriteGroup) {
        return favoriteRepository.findByUserIdAndFavoriteGroup(userId, favoriteGroup);
    }

    /**
     * 获取用户收藏分组列表
     */
    public List<String> getUserFavoriteGroups(Long userId) {
        return favoriteRepository.findFavoriteGroupsByUserId(userId);
    }

    /**
     * 检查是否已收藏
     */
    public Boolean isFavorited(Long userId, Long animeId) {
        return favoriteRepository.existsByUserIdAndAnimeId(userId, animeId);
    }

    /**
     * 获取动漫收藏次数
     */
    public Long getAnimeFavoriteCount(Long animeId) {
        return favoriteRepository.countByAnimeId(animeId);
    }

    /**
     * 获取收藏详情
     */
    public Optional<Favorite> getFavoriteDetail(Long userId, Long animeId) {
        return favoriteRepository.findByUserIdAndAnimeId(userId, animeId);
    }
}